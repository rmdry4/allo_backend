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

Contoh json

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

## 3. Add Expense — Equal Split
```
POST {{baseUrl}}/api/groups/{{groupId}}/expenses
```

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

## 4. List Expense
```
GET {{baseUrl}}/api/groups/{{groupId}}/expenses
```

