package writory.application.item.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.domain.item.ItemDomain
import writory.domain.item.entity.ItemEntity
import writory.domain.item.entity.ItemSectionEntity
import writory.domain.user.principal.UserPrincipal

@Controller
class ItemController(
        private val itemDomain: ItemDomain
) {

    @RequestMapping(method = [RequestMethod.GET], path = ["/item/{id}"])
    fun item(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            @PathVariable id: String,
            model: Model
    ): String {
        model.addAttribute("user", userPrincipal?.userEntity)

        val item: Pair<ItemEntity, List<ItemSectionEntity>> = itemDomain.find(id)
        model.addAttribute("item", item.first)
        model.addAttribute("itemSectionList", item.second)

        return "item/item"
    }

}
