description: `Order for ${cartItems.value.length} item(s) - Total: $${total.value.toFixed(2)}`,
      receipt: receipt
    }
=======
    const orderData = {
      description: `Order for ${cartItems.value.length} item(s) - Total: $${total.value.toFixed(2)}`,
      receipt: receipt,
      saveInfo: form.value.saveInfo,
      receiptEmail: form.value.receiptEmail
    }
