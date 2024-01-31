To run the server, use the command:
`gradle bootRun`

The `*Controller` classes contains the implementation of all API endpoints. The method annotations are self-explanatory.

For example, to add a product, use the `products/` POST method:
`curl -X POST http://localhost:8080/products -H "Content-type:application/json" -d "{\"name\": \"Toothpaste\", \"price\": 15.0}"`

To add a discount, use the `discounts/` POST method:
`curl -X POST http://localhost:8080/discounts -H "Content-type:application/json" -d "{\"productId\": 1, \"quantity\": 2, \"percentage\": 70}"`

To add a basket item, use the `basket/` POST method:
`curl -X POST http://localhost:8080/basket -H "Content-type:application/json" -d "{\"productId\": 1}"`

To calculate the total price of items in the basket:
`curl -v http://localhost:8080/checkout`

To run the tests, use the command:
`gradle test`