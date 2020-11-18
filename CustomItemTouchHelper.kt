import android.graphics.Canvas
import android.graphics.drawable.InsetDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

abstract class CustomItemTouchHelper(dragDirs: Int, swipeDirs: Int, private val settingsHash: HashMap<String, Any>, private val simpleCallback: ItemTouchCallBack) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs){

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        simpleCallback.callBackPosition(viewHolder.adapterPosition)

        when (direction) {
            LEFT -> {
                simpleCallback.callBackLeft()
            }
            RIGHT ->{
                simpleCallback.callBackRight()
            }
            UP -> {
                simpleCallback.callBackUp()
            }
            DOWN ->{
                simpleCallback.callBackDown()
            }
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return settingsHash["SwipeThreshold"] as Float
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * settingsHash["SwipeEscapeVelocity"] as Float
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        super.onChildDraw(c,recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val heightReduction: Int = settingsHash["ItemViewHeightReduction"] as Int
        val widthReduction: Int = settingsHash["ItemViewWidthReduction"] as Int

        val icon: InsetDrawable
        val vHeight = (itemView.height - heightReduction) / 2
        val vWidth = (itemView.width - widthReduction) / 2

        when {
            dX > 0 -> { // Right
                icon = InsetDrawable(ContextCompat.getDrawable(itemView.context, settingsHash["SwipeRightIcon"] as Int), vWidth, vHeight, vWidth, vHeight)
                icon.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
            }
            dX < 0 -> { // Left
                icon = InsetDrawable(ContextCompat.getDrawable(itemView.context, settingsHash["SwipeLeftIcon"] as Int), vWidth, vHeight, vWidth, vHeight)
                icon.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
            }
            dY < 0 -> { // UP
                icon = InsetDrawable(ContextCompat.getDrawable(itemView.context, settingsHash["SwipeUpIcon"] as Int), vWidth, vHeight, vWidth, vHeight)
                icon.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
            }
            dY > 0 -> { //DOWN
                icon = InsetDrawable(ContextCompat.getDrawable(itemView.context, settingsHash["SwipeDownIcon"] as Int), vWidth, vHeight, vWidth, vHeight)
                icon.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
            }
            else -> { // unSwiped

                icon = InsetDrawable(null, 100, 100, 100, 100)
                icon.setBounds(0, 0, 0, 0)
            }
        }
        icon.draw(c)
    }

    interface ItemTouchCallBack {
        fun callBackRight()
        fun callBackLeft()
        fun callBackUp()
        fun callBackDown()
        fun callBackPosition(position: Int):Int{
            return position
        }
    }

}