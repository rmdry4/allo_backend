# Allo Bank Backend Developer Take-Home Test

# Expense Splitter API

## Build

```
docker-compose up
```

## Run 
```
mvn spring-boot:run
```


## Test

## 1. Create Bill Group
### Skenario normal — trip 3 orang

```
POST {{baseUrl}}/api/groups

```

Format json

```
{
  "name": "Bali Trip",
  "participantNames": ["Alice", "Bob", "Carol"]
}
```
## 2. Get Bill Group
```
GET {{baseUrl}}/api/groups/{{groupId}}
```

## 3. Add Expense 
```
POST {{baseUrl}}/api/groups/{{groupId}}/expenses
```

## 3.1 Equal Split
Format json
```
{
  "paidBy": "<alice-id>",
  "amount": 300.00,
  "description": "Hotel 2 malam",
  "category": "accommodation",
  "splitType": "EQUAL",
  "splitAmong": ["<alice-id>", "<bob-id>", "<carol-id>"]
}
```

## 3.2 Percentage Split
Format json

```
{
  "paidBy": "<bob-id>",
  "amount": 200.00,
  "description": "Dinner seafood",
  "category": "food",
  "splitType": "PERCENTAGE",
  "splitAmong": ["<alice-id>", "<bob-id>"],
  "splitInput": {
    "<alice-id>": 60,
    "<bob-id>": 40
  }
}
```

## 3.3 Exact Amount Split
Format json

```
{
  "paidBy": "<carol-id>",
  "amount": 90.00,
  "description": "Groceries",
  "category": "food",
  "splitType": "EXACT",
  "splitAmong": ["<alice-id>", "<bob-id>", "<carol-id>"],
  "splitInput": {
    "<alice-id>": 20.00,
    "<bob-id>": 30.00,
    "<carol-id>": 40.00
  }
}
```

## 4. List Expense
```
GET {{baseUrl}}/api/groups/{{groupId}}/expenses
```

## 5. Record Payment
```
POST /api/groups/{groupId}/payments
```
## 5.1 Partial Payment
```
{
  "from": "<bob-id>",
  "to": "<alice-id>",
  "amount": 50.00
}
```

## 5.2 Pay All
```
{
  "from": "<carol-id>",
  "to": "<alice-id>",
  "amount": 100.00
}
```

# Submission Question

"What was the hardest design decision you made while building this, and what trade-off did you accept?"
---

Answer

The most challenging part was deciding how the application should manage and persist its data. I considered using a traditional relational database such as PostgreSQL, but ultimately chose an in-memory store using ConcurrentHashMap. The key consideration was not simply implementation speed, but the scope of the assignment: the requirements did not mandate persistence, and an in-memory approach allowed me to keep the application lightweight, self-contained, and easy to run without external infrastructure.

I also wanted this decision to demonstrate good architectural thinking rather than just taking a shortcut. I introduced repository abstractions between the business logic and the storage layer, so the services do not depend directly on ConcurrentHashMap. This means the current implementation can use ConcurrentHashMap while a future implementation can replace it with PostgreSQL/JPA without significantly changing the business logic or API layer.

The trade-off is that in-memory data is volatile and will be lost when the application restarts, so I consider this a deliberate architectural decision rather than a limitation I overlooked. For this assignment, I prioritized simplicity, testability, and clear separation of concerns while keeping the design ready for future persistence.
