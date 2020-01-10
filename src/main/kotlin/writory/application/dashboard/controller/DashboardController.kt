package writory.application.dashboard.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.application.dashboard.form.ItemDeleteForm
import writory.application.dashboard.form.ItemModifyForm
import writory.domain.item.ItemDomain
import writory.domain.item.entity.ItemEntity
import writory.domain.item.entity.ItemSectionEntity
import writory.domain.item.exception.ItemModifyException
import writory.domain.item.exception.ItemNotFoundException
import writory.domain.user.principal.UserPrincipal
import javax.servlet.http.HttpServletResponse

@Controller
class DashboardController(
        private val itemDomain: ItemDomain
) {

    @RequestMapping(method = [RequestMethod.GET], path = ["/dashboard"])
    fun getIndex(
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        return "302:/dashboard/item"
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/dashboard/item"])
    fun getItem(
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        val itemEntityList: List<ItemEntity> = itemDomain.scopeByUserIdFindAllByUserId(userPrincipal.userEntity.id!!)
        model.addAttribute("itemList", itemEntityList)

        return "200:dashboard/item"
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/dashboard/item/{id}/modify"])
    fun getItemModify(
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            form: ItemModifyForm,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        return try {
            val item: Pair<ItemEntity, List<ItemSectionEntity>> =
                    itemDomain.scopeByUserIdFindById(userPrincipal.userEntity.id!!, form.id!!)
            model.addAttribute("found", true)

            if (form.title == null) {
                form.title = item.first.title
            }

            if (form.sectionList == null) {
                form.sectionList = item.second.map {
                    ItemModifyForm.Section(
                            id = it.id,
                            header = it.header,
                            body = it.body,
                            star = it.star
                    )
                }
            }

            "200:dashboard/item-modify"
        } catch (e: ItemNotFoundException) {
            model.addAttribute("notFound", true)
            "400:dashboard/item-modify"
        }
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/dashboard/item"])
    fun postItem(
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        val itemEntity: ItemEntity = itemDomain.scopeByUserIdCreate(userPrincipal.userEntity.id!!)
        return "302:/dashboard/item/${itemEntity.id}/modify"
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/dashboard/item/{id}/delete"])
    fun postItemDelete(
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            @Validated form: ItemDeleteForm,
            bindingResult: BindingResult,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            return "400:dashboard/item-delete"
        }

        return try {
            itemDomain.scopeByUserIdDeleteById(userPrincipal.userEntity.id!!, form.id!!)
            "302:/dashboard"
        } catch (e: ItemNotFoundException) {
            "400:dashboard/item-delete"
        }
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/dashboard/item/{id}/modify"])
    fun postItemModify(
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            @Validated form: ItemModifyForm,
            bindingResult: BindingResult,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        model.addAttribute("found", true)

        if (bindingResult.hasErrors()) {
            return "400:dashboard/item-modify"
        }

        return try {
            itemDomain.scopeByUserIdModify(userPrincipal.userEntity.id!!,
                    Pair(form.id!!, ItemEntity(title = form.title)),
                    form.sectionList?.mapIndexed { index: Int, section: ItemModifyForm.Section ->
                        Pair(section.id, ItemSectionEntity(
                                position = index,
                                header = section.header,
                                body = section.body,
                                star = section.star
                        ))
                    } ?: listOf())
            "302:/dashboard"
        } catch (e: ItemModifyException) {
            "400:dashboard/item-modify"
        }
    }

}
