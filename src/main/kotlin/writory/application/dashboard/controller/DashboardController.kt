package writory.application.dashboard.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.application.dashboard.form.ItemModifyForm
import writory.domain.item.ItemDomain
import writory.domain.item.entity.ItemEntity
import writory.domain.item.entity.ItemSectionEntity
import writory.domain.item.exception.ItemModifyException
import writory.domain.user.principal.UserPrincipal

@Controller
class DashboardController(
        private val itemDomain: ItemDomain
) {

    @RequestMapping(method = [RequestMethod.GET], path = ["/dashboard"])
    fun index(
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            model: Model
    ): String {
        model.addAttribute("user", userPrincipal.userEntity)
        return "dashboard/index"
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/dashboard/item"])
    fun itemPost(
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            model: Model
    ): String {
        model.addAttribute("user", userPrincipal.userEntity)
        val itemEntity: ItemEntity = itemDomain.create(userPrincipal.userEntity.id!!)
        return "redirect:/dashboard/item/${itemEntity.id}"
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/dashboard/item/{id}"])
    fun itemModify(
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            form: ItemModifyForm,
            model: Model
    ): String {
        model.addAttribute("user", userPrincipal.userEntity)
        model.addAttribute("form", form)

        val item: Pair<ItemEntity, List<ItemSectionEntity>> = itemDomain.find(userPrincipal.userEntity.id!!, form.id!!)

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

        return "dashboard/item-modify"
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/dashboard/item/{id}"])
    fun itemModifyPost(
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            @Validated form: ItemModifyForm,
            bindingResult: BindingResult,
            model: Model
    ): String {
        model.addAttribute("user", userPrincipal.userEntity)
        model.addAttribute("form", form)

        if (bindingResult.hasErrors()) {
            return "dashboard/item-modify"
        }

        return try {
            itemDomain.modify(userPrincipal.userEntity.id!!,
                    Pair(form.id!!, ItemEntity(title = form.title)),
                    form.sectionList?.mapIndexed { index: Int, section: ItemModifyForm.Section ->
                        Pair(section.id, ItemSectionEntity(
                                position = index,
                                header = section.header,
                                body = section.body,
                                star = section.star
                        ))
                    } ?: listOf())
            "redirect:/dashboard"
        } catch (e: ItemModifyException) {
            "authentication/sign-up"
        }
    }

}
