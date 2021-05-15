package com.felipheallef.elocations.data

/**
 * Data validation state of the business form.
 */
data class BusinessFormState(
    val nameError: Int? = null,
    val descriptionError: Int? = null,
    val categoryError: Int? = null,
    val isDataValid: Boolean = false)