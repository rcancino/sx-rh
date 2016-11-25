<html>
<head>
  <meta charset="UTF-8">
  <meta name="layout" content="editForm"/>
  <title>Alta de comisiones</title>
  <g:set var="entity" value="${registroDeComisionesInstance}" scope="request" />
  <g:set var="editable" value="${true}" scope="request" />
  <g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

<content tag="header">
  Solicitud de vacaciones
</content>

<content tag="formFields">
  <f:with bean="registroDeComisionesInstance">
    <f:display property="empleado" />
    <f:field property="calendarioDet" />
    <f:field property="importe" widget="money"/>
    <f:field property="comentario" widget-class="form-control"/>
  </f:with>
</content>
  
</body>
</html>


