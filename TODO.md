## Backend Changes

- [x] Create Order.java entity with fields: id, userAccount (ManyToOne), orderDate, description, receiptJson
- [x] Create OrderRepository.java for data access
- [x] Create OrderController.java with GET /api/orders to fetch user's order history
- [x] Modify CheckoutView.vue to send order data to backend on placeOrder (integrate with backend order placement)

## Frontend Changes

- [x] Create HistoryView.vue to display order history with date, description, and receipt
- [x] Add /history route in router/index.js
- [x] Update navigation to include History link (possibly in App.vue for navigation)

## Testing and Followup

- [x] Test the order placement and history retrieval
- [x] Ensure no conflicts with existing cart functionality
