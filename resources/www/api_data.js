define({ "api": [
  {
    "type": "get",
    "url": "/v1/orders_batch/payment_defined",
    "title": "Batch Payment Defined",
    "name": "Batch_Payment_Defined",
    "group": "Ordenes",
    "description": "<p>Ejecuta un proceso batch que chequea ordenes en estado PAYMENT_DEFINED.</p>",
    "success": {
      "examples": [
        {
          "title": "Respuesta",
          "content": "HTTP/1.1 200 OK",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "src/application/RestController.java",
    "groupTitle": "Ordenes",
    "examples": [
      {
        "title": "Header Autorización",
        "content": "Authorization=bearer {token}",
        "type": "String"
      }
    ],
    "error": {
      "examples": [
        {
          "title": "401 Unauthorized",
          "content": "HTTP/1.1 401 Unauthorized",
          "type": "json"
        },
        {
          "title": "400 Bad Request",
          "content": "HTTP/1.1 400 Bad Request\n{\n    \"path\" : \"{Nombre de la propiedad}\",\n    \"message\" : \"{Motivo del error}\"\n}",
          "type": "json"
        },
        {
          "title": "400 Bad Request",
          "content": "HTTP/1.1 400 Bad Request\n{\n    \"error\" : \"{Motivo del error}\"\n}",
          "type": "json"
        },
        {
          "title": "500 Server Error",
          "content": "HTTP/1.1 500 Server Error\n{\n    \"error\" : \"{Motivo del error}\"\n}",
          "type": "json"
        }
      ]
    }
  },
  {
    "type": "get",
    "url": "/v1/orders_batch/placed",
    "title": "Batch Placed",
    "name": "Batch_Placed",
    "group": "Ordenes",
    "description": "<p>Ejecuta un proceso batch que chequea ordenes en estado PLACED.</p>",
    "success": {
      "examples": [
        {
          "title": "Respuesta",
          "content": "HTTP/1.1 200 OK",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "src/application/RestController.java",
    "groupTitle": "Ordenes",
    "examples": [
      {
        "title": "Header Autorización",
        "content": "Authorization=bearer {token}",
        "type": "String"
      }
    ],
    "error": {
      "examples": [
        {
          "title": "401 Unauthorized",
          "content": "HTTP/1.1 401 Unauthorized",
          "type": "json"
        },
        {
          "title": "400 Bad Request",
          "content": "HTTP/1.1 400 Bad Request\n{\n    \"path\" : \"{Nombre de la propiedad}\",\n    \"message\" : \"{Motivo del error}\"\n}",
          "type": "json"
        },
        {
          "title": "400 Bad Request",
          "content": "HTTP/1.1 400 Bad Request\n{\n    \"error\" : \"{Motivo del error}\"\n}",
          "type": "json"
        },
        {
          "title": "500 Server Error",
          "content": "HTTP/1.1 500 Server Error\n{\n    \"error\" : \"{Motivo del error}\"\n}",
          "type": "json"
        }
      ]
    }
  },
  {
    "type": "get",
    "url": "/v1/orders_batch/validated",
    "title": "Batch Validated",
    "name": "Batch_Validated",
    "group": "Ordenes",
    "description": "<p>Ejecuta un proceso batch para ordenes en estado VALIDATED.</p>",
    "success": {
      "examples": [
        {
          "title": "Respuesta",
          "content": "HTTP/1.1 200 OK",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "src/application/RestController.java",
    "groupTitle": "Ordenes",
    "examples": [
      {
        "title": "Header Autorización",
        "content": "Authorization=bearer {token}",
        "type": "String"
      }
    ],
    "error": {
      "examples": [
        {
          "title": "401 Unauthorized",
          "content": "HTTP/1.1 401 Unauthorized",
          "type": "json"
        },
        {
          "title": "400 Bad Request",
          "content": "HTTP/1.1 400 Bad Request\n{\n    \"path\" : \"{Nombre de la propiedad}\",\n    \"message\" : \"{Motivo del error}\"\n}",
          "type": "json"
        },
        {
          "title": "400 Bad Request",
          "content": "HTTP/1.1 400 Bad Request\n{\n    \"error\" : \"{Motivo del error}\"\n}",
          "type": "json"
        },
        {
          "title": "500 Server Error",
          "content": "HTTP/1.1 500 Server Error\n{\n    \"error\" : \"{Motivo del error}\"\n}",
          "type": "json"
        }
      ]
    }
  },
  {
    "type": "get",
    "url": "/v1/orders/:orderId",
    "title": "Buscar Orden",
    "name": "Buscar_Orden",
    "group": "Ordenes",
    "description": "<p>Busca una order del usuario logueado, por su id.</p>",
    "success": {
      "examples": [
        {
          "title": "Respuesta",
          "content": "HTTP/1.1 200 OK\n{\n   \"id\": \"{orderID}\",\n   \"status\": \"{Status}\",\n   \"cartId\": \"{cartId}\",\n   \"updated\": \"{updated date}\",\n   \"created\": \"{created date}\",\n   \"articles\": [\n      {\n          \"id\": \"{articleId}\",\n          \"quantity\": {quantity},\n          \"validated\": true|false,\n          \"valid\": true|false\n      }, ...\n  ]\n}",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "src/application/RestController.java",
    "groupTitle": "Ordenes",
    "examples": [
      {
        "title": "Header Autorización",
        "content": "Authorization=bearer {token}",
        "type": "String"
      }
    ],
    "error": {
      "examples": [
        {
          "title": "401 Unauthorized",
          "content": "HTTP/1.1 401 Unauthorized",
          "type": "json"
        },
        {
          "title": "400 Bad Request",
          "content": "HTTP/1.1 400 Bad Request\n{\n    \"path\" : \"{Nombre de la propiedad}\",\n    \"message\" : \"{Motivo del error}\"\n}",
          "type": "json"
        },
        {
          "title": "400 Bad Request",
          "content": "HTTP/1.1 400 Bad Request\n{\n    \"error\" : \"{Motivo del error}\"\n}",
          "type": "json"
        },
        {
          "title": "500 Server Error",
          "content": "HTTP/1.1 500 Server Error\n{\n    \"error\" : \"{Motivo del error}\"\n}",
          "type": "json"
        }
      ]
    }
  },
  {
    "type": "get",
    "url": "/v1/orders",
    "title": "Ordenes de Usuario",
    "name": "Ordenes_de_Usuario",
    "group": "Ordenes",
    "description": "<p>Busca todas las ordenes del usuario logueado.</p>",
    "success": {
      "examples": [
        {
          "title": "Respuesta",
          "content": "HTTP/1.1 200 OK\n[{\n   \"id\": \"{orderID}\",\n   \"status\": \"{Status}\",\n   \"cartId\": \"{cartId}\",\n   \"updated\": \"{updated date}\",\n   \"created\": \"{created date}\",\n   \"totalPrice\": {price}\n   \"articles\": {count}\n}, ...\n]",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "src/application/RestController.java",
    "groupTitle": "Ordenes",
    "examples": [
      {
        "title": "Header Autorización",
        "content": "Authorization=bearer {token}",
        "type": "String"
      }
    ],
    "error": {
      "examples": [
        {
          "title": "401 Unauthorized",
          "content": "HTTP/1.1 401 Unauthorized",
          "type": "json"
        },
        {
          "title": "400 Bad Request",
          "content": "HTTP/1.1 400 Bad Request\n{\n    \"path\" : \"{Nombre de la propiedad}\",\n    \"message\" : \"{Motivo del error}\"\n}",
          "type": "json"
        },
        {
          "title": "400 Bad Request",
          "content": "HTTP/1.1 400 Bad Request\n{\n    \"error\" : \"{Motivo del error}\"\n}",
          "type": "json"
        },
        {
          "title": "500 Server Error",
          "content": "HTTP/1.1 500 Server Error\n{\n    \"error\" : \"{Motivo del error}\"\n}",
          "type": "json"
        }
      ]
    }
  },
  {
    "type": "post",
    "url": "/v1/orders/:orderId/payment",
    "title": "Agregar Pago",
    "name": "Agrega_un_Pago",
    "group": "Pagos",
    "examples": [
      {
        "title": "Body",
        "content": "{\n    \"paymentMethod\": \"CASH | CREDIT | DEBIT\",\n    \"amount\": \"{amount}\"\n}",
        "type": "json"
      },
      {
        "title": "Header Autorización",
        "content": "Authorization=bearer {token}",
        "type": "String"
      }
    ],
    "success": {
      "examples": [
        {
          "title": "Respuesta",
          "content": "HTTP/1.1 200 OK",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "src/application/RestController.java",
    "groupTitle": "Pagos",
    "error": {
      "examples": [
        {
          "title": "401 Unauthorized",
          "content": "HTTP/1.1 401 Unauthorized",
          "type": "json"
        },
        {
          "title": "400 Bad Request",
          "content": "HTTP/1.1 400 Bad Request\n{\n    \"path\" : \"{Nombre de la propiedad}\",\n    \"message\" : \"{Motivo del error}\"\n}",
          "type": "json"
        },
        {
          "title": "400 Bad Request",
          "content": "HTTP/1.1 400 Bad Request\n{\n    \"error\" : \"{Motivo del error}\"\n}",
          "type": "json"
        },
        {
          "title": "500 Server Error",
          "content": "HTTP/1.1 500 Server Error\n{\n    \"error\" : \"{Motivo del error}\"\n}",
          "type": "json"
        }
      ]
    }
  },
  {
    "type": "direct",
    "url": "order/article-data",
    "title": "Validar Artículos",
    "group": "RabbitMQ_GET",
    "description": "<p>Antes de iniciar las operaciones se validan los artículos contra el catalogo.</p>",
    "examples": [
      {
        "title": "Mensaje",
        "content": "{\n\"type\": \"article-data\",\n\"message\" : {\n    \"cartId\": \"{cartId}\",\n    \"articleId\": \"{articleId}\",\n    \"valid\": True|False\n   }\n}",
        "type": "json"
      }
    ],
    "version": "0.0.0",
    "filename": "src/application/RabbitController.java",
    "groupTitle": "RabbitMQ_GET",
    "name": "DirectOrderArticleData"
  },
  {
    "type": "direct",
    "url": "order/place-order",
    "title": "Crear Orden",
    "group": "RabbitMQ_GET",
    "description": "<p>Escucha de mensajes place-order en el canal de order.</p>",
    "examples": [
      {
        "title": "Mensaje",
        "content": "{\n\"type\": \"place-order\",\n\"exchange\" : \"{Exchange name to reply}\"\n\"queue\" : \"{Queue name to reply}\"\n\"message\" : {\n    \"cartId\": \"{cartId}\",\n    \"articles\": \"[articleId, ...]\",\n}",
        "type": "json"
      }
    ],
    "version": "0.0.0",
    "filename": "src/application/RabbitController.java",
    "groupTitle": "RabbitMQ_GET",
    "name": "DirectOrderPlaceOrder"
  },
  {
    "type": "fanout",
    "url": "auth/logout",
    "title": "Logout",
    "group": "RabbitMQ_GET",
    "description": "<p>Escucha de mensajes logout desde auth. Invalida sesiones en cache.</p>",
    "examples": [
      {
        "title": "Mensaje",
        "content": "{\n  \"type\": \"logout\",\n  \"message\" : \"tokenId\"\n}",
        "type": "json"
      }
    ],
    "version": "0.0.0",
    "filename": "src/application/RabbitController.java",
    "groupTitle": "RabbitMQ_GET",
    "name": "FanoutAuthLogout"
  },
  {
    "type": "direct",
    "url": "cart/article-data",
    "title": "Validación de Artículos",
    "group": "RabbitMQ_POST",
    "description": "<p>Antes de iniciar las operaciones se validan los artículos contra el catalogo.</p>",
    "success": {
      "examples": [
        {
          "title": "Mensaje",
          "content": "{\n\"type\": \"article-data\",\n\"message\" : {\n    \"cartId\": \"{cartId}\",\n    \"articleId\": \"{articleId}\",\n   }\n}",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "src/application/RabbitController.java",
    "groupTitle": "RabbitMQ_POST",
    "name": "DirectCartArticleData"
  },
  {
    "type": "topic",
    "url": "order/order-placed",
    "title": "Orden Creada",
    "group": "RabbitMQ_POST",
    "description": "<p>Envía de mensajes order-placed desde Order con el topic &quot;order_placed&quot;.</p>",
    "success": {
      "examples": [
        {
          "title": "Mensaje",
          "content": "{\n\"type\": \"order-placed\",\n\"message\" : {\n    \"cartId\": \"{cartId}\",\n    \"orderId\": \"{orderId}\"\n    \"articles\": [{\n         \"articleId\": \"{article id}\"\n         \"quantity\" : {quantity}\n     }, ...]\n   }\n}",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "src/application/RabbitController.java",
    "groupTitle": "RabbitMQ_POST",
    "name": "TopicOrderOrderPlaced"
  }
] });
