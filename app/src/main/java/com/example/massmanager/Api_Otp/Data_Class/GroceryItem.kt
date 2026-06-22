package com.example.massmanager.Api_Otp.Data_Class



// ১. একক আইটেমের মডেল
data class GroceryItem(
    val name: String,
    val weight: String,
    val price: String
)

// ২. পুরো রিকোয়েস্ট বডি (যা PHP-তে পাঠানো হবে)
data class GroceryRequest(
    val name: String,
    val email: String,
    val userid: String,
    val date: String,
    val items: List<GroceryItem>
)

// ৩. সার্ভার থেকে আসা রেসপন্স মডেল
data class BasicResponse(
    val success: Boolean,
    val message: String
)