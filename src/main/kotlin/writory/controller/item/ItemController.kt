package writory.controller.item

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import writory.entity.ItemEntity
import writory.entity.ItemSectionEntity
import writory.principal.UserPrincipal
import writory.service.item.ItemService

@Controller
class ItemController(
        private val itemService: ItemService
) {

    @RequestMapping(method = [RequestMethod.GET], path = ["/item/{id}"])
    fun item(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            @PathVariable id: String,
            model: Model
    ): String {
        model.addAttribute("user", userPrincipal?.userEntity)

        val item: Pair<ItemEntity, List<ItemSectionEntity>> = itemService.findItem(id)
        model.addAttribute("item", item.first)
        model.addAttribute("itemSectionList", item.second)

        return "item/item"
    }

}
