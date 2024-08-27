 # CashCalm
 
 This project is a financial system designed to assist users in managing and categorizing their financial transactions. It aims to provide a comprehensive solution for tracking income and expenses, ensuring accurate and organized financial management.
 
 ## OpenAPI definition
 
 Version: v1
 
 All rights reserved
 
 [License](http://apache.org/licenses/LICENSE-2.0.html)
 
 ## Access
 
 ## Methods
 
 ### Table of Contents
 
 #### [LabelController](#LabelController)
 
 - [`post /labels`](#createLabel)
 - [`delete /labels/{id}`](#deleteLabel)
 - [`get /labels/{id}`](#getLabel)
 - [`get /labels`](#getLabels)
 - [`put /labels`](#updateLabel)
 
 #### [TransactionController](#TransactionController)
 
 - [`post /transactions`](#createTransaction)
 - [`delete /transactions/{id}`](#deleteTransaction)
 - [`get /transactions/pdf`](#getPdfFile)
 - [`get /transactions`](#getTransactions)
 - [`post /transactions/parse`](#parseTransactions)
 - [`put /transactions`](#updateTransaction)
 
 #### [TransactionGroupController](#TransactionGroupController)
 
 - [`post /transaction-groups`](#createTransactionGroup)
 - [`delete /transaction-groups/{id}`](#deleteTransactionGroup)
 - [`get /transaction-groups`](#getAllTransactionGroups)
 - [`put /transaction-groups`](#updateTransactionGroup)
 
 #### [UserController](#UserController)
 
 - [`delete /users`](#deleteUser)
 - [`post /forgot-password`](#forgotPassword)
 - [`get /users`](#getUsers)
 - [`post /register`](#registerUser)
 - [`post /reset-password`](#resetPassword)
 - [`post /verify`](#verifyUser)
 
 ## LabelController
 
 [Up](#__Methods)
 
 #### Create a label
 
 ```http
 POST /labels
 ```
 
 **Consumes:**
 
 - `application/json`
 
 **Request body:**
 
 | Parameter | Type   | Description       |
 | :-------- | :----- | :---------------- |
 | `name`    | `json` | **Required**. Name of the label |
 | `id`      | `int`  | **Required**. ID of the label   |
 
 **Response:**
 
 - 200 OK
 
 #### Delete a label by ID
 
 ```http
 DELETE /labels/{id}
 ```
 
 | Parameter | Type  | Description              |
 | :-------- | :---- | :----------------------- |
 | `id`      | `int` | **Required**. ID of the label to delete |
 
 **Response:**
 
 - 200 OK
 
 #### Get a label by ID
 
 ```http
 GET /labels/{id}
 ```
 
 | Parameter | Type  | Description            |
 | :-------- | :---- | :--------------------- |
 | `id`      | `int` | **Required**. ID of the label to fetch |
 
 **Response:**
 
 - 200 OK
 
 #### Get all labels
 
 ```http
 GET /labels
 ```
 
 **Response:**
 
 - 200 OK
 
 #### Update a label
 
 ```http
 PUT /labels
 ```
 
 **Consumes:**
 
 - `application/json`
 
 **Request body:**
 
 | Parameter | Type   | Description       |
 | :-------- | :----- | :---------------- |
 | `name`    | `json` | **Required**. Name of the label |
 | `id`      | `int`  | **Required**. ID of the label   |
 
 **Response:**
 
 - 200 OK
 
 ## TransactionController
 
 [Up](#__Methods)
 
 #### Create a transaction
 
 ```http
 POST /transactions
 ```
 
 **Consumes:**
 
 - `application/json`
 
 **Request body:**
 
 | Parameter | Type         | Description                 |
 | :-------- | :----------- | :-------------------------- |
 | `amount`  | `json`       | **Required**. Amount        |
 | `balance` | `json`       | **Required**. Balance       |
 | `label`   | `json`       | **Required**. Label object  |
 
 **Response:**
 
 - 200 OK
 
 #### Delete a transaction by ID
 
 ```http
 DELETE /transactions/{id}
 ```
 
 | Parameter | Type  | Description                   |
 | :-------- | :---- | :---------------------------- |
 | `id`      | `int` | **Required**. ID of the transaction to delete |
 
 **Response:**
 
 - 200 OK
 
 #### Get transactions as PDF
 
 ```http
 GET /transactions/pdf
 ```
 
 | Parameter | Type     | Description                       |
 | :-------- | :------- | :-------------------------------- |
 | `from`    | `date`   | **Optional**. Start date          |
 | `to`      | `date`   | **Optional**. End date            |
 | `label`   | `string` | **Optional**. Filter by label     |
 
 **Response:**
 
 - 200 OK
 
 #### Get all transactions
 
 ```http
 GET /transactions
 ```
 
 | Parameter | Type     | Description                       |
 | :-------- | :------- | :-------------------------------- |
 | `from`    | `date`   | **Optional**. Start date          |
 | `to`      | `date`   | **Optional**. End date            |
 | `label`   | `string` | **Optional**. Filter by label     |
 | `group`   | `string` | **Optional**. Filter by group     |
 
 **Response:**
 
 - 200 OK
 
 #### Parse transactions
 
 ```http
 POST /transactions/parse
 ```
 
 **Consumes:**
 
 - `application/json`
 
 **Response:**
 
 - 200 OK
 
 #### Update a transaction
 
 ```http
 PUT /transactions
 ```
 
 **Consumes:**
 
 - `application/json`
 
 **Request body:**
 
 | Parameter | Type         | Description                 |
 | :-------- | :----------- | :-------------------------- |
 | `amount`  | `json`       | **Required**. Amount        |
 | `balance` | `json`       | **Required**. Balance       |
 | `label`   | `json`       | **Required**. Label object  |
 
 **Response:**
 
 - 200 OK
 
 ## TransactionGroupController
 
 [Up](#__Methods)
 
 #### Create a transaction group
 
 ```http
 POST /transaction-groups
 ```
 
 **Consumes:**
 
 - `application/json`
 
 **Request body:**
 
 | Parameter | Type         | Description                  |
 | :-------- | :----------- | :--------------------------- |
 | `name`    | `string`     | **Required**. Group name     |
 
 **Response:**
 
 - 200 OK
 
 #### Delete a transaction group by ID
 
 ```http
 DELETE /transaction-groups/{id}
 ```
 
 | Parameter | Type  | Description                       |
 | :-------- | :---- | :-------------------------------- |
 | `id`      | `int` | **Required**. ID of the transaction group to delete |
 
 **Response:**
 
 - 200 OK
 
 #### Get all transaction groups
 
 ```http
 GET /transaction-groups
 ```
 
 **Response:**
 
 - 200 OK
 
 #### Update a transaction group
 
 ```http
 PUT /transaction-groups
 ```
 
 **Consumes:**
 
 - `application/json`
 
 **Request body:**
 
 | Parameter | Type         | Description                  |
 | :-------- | :----------- | :--------------------------- |
 | `name`    | `string`     | **Required**. Group name     |
 
 **Response:**
 
 - 200 OK
 
 ## UserController
 
 [Up](#__Methods)
 
 #### Delete a user
 
 ```http
 DELETE /users
 ```
 
 **Response:**
 
 - 200 OK
 
 #### Forgot password
 
 ```http
 POST /forgot-password
 ```
 
 **Consumes:**
 
 - `application/json`
 
 **Request body:**
 
 | Parameter | Type         | Description                  |
 | :-------- | :----------- | :--------------------------- |
 | `email`   | `string`     | **Required**. User email     |
 
 **Response:**
 
 - 200 OK
 
 #### Get all users
 
 ```http
 GET /users
 ```
 
 **Response:**
 
 - 200 OK
 
 #### Register a user
 
 ```http
 POST /register
 ```
 
 **Consumes:**
 
 - `application/json`
 
 **Request body:**
 
 | Parameter | Type         | Description                  |
 | :-------- | :----------- | :--------------------------- |
 | `email`   | `string`     | **Required**. User email     |
 | `password`| `string`     | **Required**. User password  |
 
 **Response:**
 
 - 200 OK
 
 #### Reset password
 
 ```http
 POST /reset-password
 ```
 
 **Consumes:**
 
 - `application/json`
 
 **Request body:**
 
 | Parameter | Type         | Description                  |
 | :-------- | :----------- | :--------------------------- |
 | `email`   | `string`     | **Required**. User email     |
 | `token`   | `string`     | **Required**. Reset token    |
 | `new_password` | `string`| **Required**. New password   |
 
 **Response:**
 
 - 200 OK
 
 #### Verify a user
 
 ```http
 POST /verify
 ```
 
 **Consumes:**
 
 - `application/json`
 
 **Request body:**
 
 | Parameter | Type         | Description                  |
 | :-------- | :----------- | :--------------------------- |
 | `token`   | `string`     | **Required**. Verification token |
 
 **Response:**
 
 - 200 OK
