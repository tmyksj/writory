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
import javax.servlet.http.HttpServletResponse

@Controller
class ItemController(
        private val itemDomain: ItemDomain
) {

    @RequestMapping(method = [RequestMethod.GET], path = ["/item/{id}"])
    fun getItem(
            @AuthenticationPrincipal userPrincipal: UserPrincipal?,
            form: ItemForm,
            httpServletResponse: HttpServletResponse,
            model: Model
    ): String {
        return try {
            val item: Pair<ItemEntity, List<ItemSectionEntity>> = itemDomain.findById(form.id!!)
            model.addAttribute("item", item.first)
            model.addAttribute("itemSectionList", item.second.filter { form.all != null || it.star == true })
            "200:item/item"
        } catch (e: ItemNotFoundException) {
            "404:item/item"
        }
    }

}
