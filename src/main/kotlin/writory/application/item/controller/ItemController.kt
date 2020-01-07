package writory.application.item.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.application.item.form.ItemForm
import writory.domain.item.ItemDomain
import writory.domain.item.entity.ItemEntity
import writory.domain.item.entity.ItemSectionEntity
import writory.domain.item.exception.ItemNotFoundException
import writory.domain.user.principal.UserPrincipal

@Controller
class ItemController(
        private val itemDomain: ItemDomain
) {

    @RequestMapping(method = [RequestMethod.GET], path = ["/item/{id}"])
    fun item(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            form: ItemForm,
            model: Model
    ): String {
        model.addAttribute("user", userPrincipal?.userEntity)
        model.addAttribute("form", form)

        try {
            val item: Pair<ItemEntity, List<ItemSectionEntity>> = itemDomain.findById(form.id!!)
            model.addAttribute("itemFound", true)
            model.addAttribute("item", item.first)

            if (form.all == null) {
                model.addAttribute("itemSectionList", item.second.filter { it.star == true })
            } else {
                model.addAttribute("itemSectionList", item.second)
            }
        } catch (e: ItemNotFoundException) {
            model.addAttribute("itemFound", false)
        }

        return "item/item"
    }

}
