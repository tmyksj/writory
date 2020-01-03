package writory.controller.dashboard

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.entity.ItemEntity
import writory.entity.ItemSectionEntity
import writory.exception.dashboard.ItemModifyException
import writory.form.dashboard.ItemModifyForm
import writory.principal.UserPrincipal
import writory.service.dashboard.DashboardService

@Controller
class DashboardController(
        private val dashboardService: DashboardService
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
        val itemEntity: ItemEntity = dashboardService.createItem(userPrincipal.userEntity.id!!)
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

        val item: Pair<ItemEntity, List<ItemSectionEntity>> =
                dashboardService.findItem(userPrincipal.userEntity.id!!, form.id!!)

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
            dashboardService.modifyItem(userPrincipal.userEntity.id!!,
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
