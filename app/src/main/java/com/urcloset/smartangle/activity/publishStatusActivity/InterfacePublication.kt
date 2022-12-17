package com.urcloset.smartangle.activity.publishStatusActivity
import android.view.View
import androidx.core.content.ContextCompat
import com.skydoves.sandwich.ApiResponse
import com.urcloset.smartangle.R
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.ProductPublishStateLayoutBinding
import com.urcloset.smartangle.databinding.PublishStateItemBinding
import com.urcloset.smartangle.model.ProductItemResponse
import com.urcloset.smartangle.model.PublishStateModel

interface InterfacePublication {
    suspend fun getCurrentList(
        dataSource: AppApi
    ) : ApiResponse<ProductItemResponse>
    fun showHideViews(binding: PublishStateItemBinding)
}
class PublicationOrdersImplementer() : InterfacePublication {
    override suspend fun getCurrentList(
        dataSource: AppApi): ApiResponse<ProductItemResponse> {
        return  dataSource.getPublicationStatus()
    }

    override fun showHideViews(binding: PublishStateItemBinding) { // the default operation checked are here
        binding.ivIconState.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.published_icon))
        binding.tvState.text = binding.root.context.getString(R.string.published)
    }
}

class SoldOrdersImplementer() : InterfacePublication {
    override suspend fun getCurrentList(
        dataSource: AppApi,
    ): ApiResponse<ProductItemResponse> {

        return  dataSource.getSelledProducts()
    }
    override fun showHideViews(binding: PublishStateItemBinding) {
        binding.ivIconState.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_sold_drawable))
        binding.tvState.text = binding.root.context.getString(R.string.sold)
        binding.cvDelete.visibility = View.GONE
        binding.cvEdit.visibility = View.GONE
        binding.lyEdit.visibility = View.GONE
        binding.cvEditandpublish.visibility = View.GONE
    }

}
class RejectedProductsImplementer : InterfacePublication {
    override suspend fun getCurrentList(
        dataSource: AppApi,
    ): ApiResponse<ProductItemResponse> {

        return  dataSource.getRejected()
    }
    override fun showHideViews(binding: PublishStateItemBinding) {
        binding.ivIconState.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_rejected_products))
        binding.tvState.text = binding.root.context.getString(R.string.rejected)
        binding.cvDelete.visibility = View.GONE
        binding.cvEdit.visibility = View.GONE
        binding.lyEdit.visibility = View.GONE
        binding.cvEditandpublish.visibility = View.VISIBLE
    }

}
