<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<title>Alta de dia festivo</title>
	<g:set var="entity" value="${diaFestivoInstance}" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

<content tag="header">
	Alta de día festivo (Ejercicio${session.ejercicio })
</content>

<content tag="formFields">
	<f:with bean="diaFestivoInstance">
		<f:field property="ejercicio" widget-class="form-control"/>
		<f:field property="fecha"  />
		<f:field property="descripcion" widget-class="form-control" />
		<f:field property="parcial" widget-class="form-control" />
		<f:field property="salida" widget-class="form-control" >
		<input class="time " 
			type="text" 
			name="salida" 
			value="${diaFestivoInstance.salida}"  />

			</f:field>
	</f:with>

</content>
	
</body>
</html>


