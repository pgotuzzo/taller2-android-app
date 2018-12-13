package ar.uba.fi.tallerii.comprameli.domain.products

import android.os.Parcel
import android.os.Parcelable

data class Product(val productId: String,
                   val title: String,
                   val description: String,
                   val images: List<String>,
                   val price: Float,
                   val units: Int,
                   val longitude: Double,
                   val latitude: Double,
                   val categories: List<String>,
                   val paymentMethods: List<PaymentMethod> = ArrayList(),
                   val questions: List<Question> = ArrayList(),
                   val seller: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.createStringArrayList()!!,
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.createStringArrayList()!!,
            parcel.createTypedArrayList(PaymentMethod)!!,
            parcel.createTypedArrayList(Question)!!,
            parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeStringList(images)
        parcel.writeFloat(price)
        parcel.writeInt(units)
        parcel.writeDouble(longitude)
        parcel.writeDouble(latitude)
        parcel.writeStringList(categories)
        parcel.writeTypedList(paymentMethods)
        parcel.writeTypedList(questions)
        parcel.writeString(seller)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }

}
