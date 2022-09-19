package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("dataArray")
    var dataArray: ArrayList<DataArray>? = null
) {
    data class DataArray(
        @SerializedName("activity")
        var activity: String? = null,
        @SerializedName("add_date")
        var addDate: String? = null,
        @SerializedName("address")
        var address: String? = null,
        @SerializedName("bank_statement")
        var bankStatement: String? = null,
        @SerializedName("city")
        var city: String? = null,
        @SerializedName("company_name")
        var companyName: String? = null,
        @SerializedName("country_id")
        var countryId: String? = null,
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("del")
        var del: String? = null,
        @SerializedName("dob")
        var dob: String? = null,
        @SerializedName("email")
        var email: String? = null,
        @SerializedName("employee_id")
        var employeeId: String? = null,
        @SerializedName("first_name")
        var firstName: String? = null,
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("id_photo")
        var idPhoto: String? = null,
        @SerializedName("Ins")
        var ins: String? = null,
        @SerializedName("lang")
        var lang: String? = null,
        @SerializedName("last_name")
        var lastName: String? = null,
        @SerializedName("location")
        var location: String? = null,
        @SerializedName("main_department")
        var mainDepartment: String? = null,
        @SerializedName("mobile")
        var mobile: String? = null,
        @SerializedName("nationality_id")
        var nationalityId: String? = null,
        @SerializedName("nudv")
        var nudv: String? = null,
        @SerializedName("password")
        var password: String? = null,
        @SerializedName("phone")
        var phone: String? = null,
        @SerializedName("remember_token")
        var rememberToken: String? = null,
        @SerializedName("rep")
        var rep: String? = null,
        @SerializedName("responsible")
        var responsible: String? = null,
        @SerializedName("salary_letter")
        var salaryLetter: String? = null,
        @SerializedName("sex")
        var sex: String? = null,
        @SerializedName("subdep")
        var subdep: String? = null,
        @SerializedName("type")
        var type: String? = null,
        @SerializedName("Upd")
        var upd: String? = null,
        @SerializedName("updated_at")
        var updatedAt: String? = null,
        @SerializedName("username")
        var username: String? = null,
        @SerializedName("validation_code")
        var validationCode: String? = null,
        @SerializedName("verified")
        var verified: String? = null,
        @SerializedName("welcome")
        var welcome: String? = null,
        @SerializedName("0")
        var x0: String? = null,
        @SerializedName("1")
        var x1: String? = null,
        @SerializedName("10")
        var x10: String? = null,
        @SerializedName("11")
        var x11: String? = null,
        @SerializedName("12")
        var x12: String? = null,
        @SerializedName("13")
        var x13: String? = null,
        @SerializedName("14")
        var x14: String? = null,
        @SerializedName("15")
        var x15: String? = null,
        @SerializedName("16")
        var x16: String? = null,
        @SerializedName("17")
        var x17: String? = null,
        @SerializedName("18")
        var x18: String? = null,
        @SerializedName("19")
        var x19: String? = null,
        @SerializedName("2")
        var x2: String? = null,
        @SerializedName("20")
        var x20: String? = null,
        @SerializedName("21")
        var x21: String? = null,
        @SerializedName("22")
        var x22: String? = null,
        @SerializedName("23")
        var x23: String? = null,
        @SerializedName("24")
        var x24: String? = null,
        @SerializedName("25")
        var x25: String? = null,
        @SerializedName("26")
        var x26: String? = null,
        @SerializedName("27")
        var x27: String? = null,
        @SerializedName("28")
        var x28: String? = null,
        @SerializedName("29")
        var x29: String? = null,
        @SerializedName("3")
        var x3: String? = null,
        @SerializedName("30")
        var x30: String? = null,
        @SerializedName("31")
        var x31: String? = null,
        @SerializedName("32")
        var x32: String? = null,
        @SerializedName("33")
        var x33: String? = null,
        @SerializedName("34")
        var x34: String? = null,
        @SerializedName("35")
        var x35: String? = null,
        @SerializedName("36")
        var x36: String? = null,
        @SerializedName("37")
        var x37: String? = null,
        @SerializedName("38")
        var x38: String? = null,
        @SerializedName("4")
        var x4: String? = null,
        @SerializedName("5")
        var x5: String? = null,
        @SerializedName("6")
        var x6: String? = null,
        @SerializedName("7")
        var x7: String? = null,
        @SerializedName("8")
        var x8: String? = null,
        @SerializedName("9")
        var x9: String? = null,
        @SerializedName("zip_code")
        var zipCode: String? = null
    )
}