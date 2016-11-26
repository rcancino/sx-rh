<html>
<head>
  <meta charset="UTF-8">
  <meta name="layout" content="showForm"/>
  <title>Alta de comisiones</title>
  <g:set var="entity" value="${registroDeComisionesInstance}" scope="request" />
  <g:set var="editable" value="${true}" scope="request" />
  <g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

<content tag="header">
  Solicitud de vacaciones
</content>

<content tag="form">
  <f:with bean="registroDeComisionesInstance">
    <f:display property="empleado" />
    <f:display property="calendarioDet" />
    <f:display property="importe" widget="money"/>
    <f:display property="comentario" widget-class="form-control"/>
  </f:with>
</content>
  
</body>
</html>


