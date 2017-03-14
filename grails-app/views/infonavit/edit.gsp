<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="editForm"/>
	<g:set var="entity" value="${infonavitInstance}" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<title>INFONAVIT ${infonavitInstance.id }</title>
	
</head>
<body>
	<content tag="header">
		CréditoINFONAVIT de: ${infonavitInstance.empleado}
	</content>
	
	<content tag="formTitle">
		Crédito INFONAVIT
	</content>
	
	<content tag="formFields">
		<f:with bean="${infonavitInstance}">
			<f:field property="alta" />
			<f:field property="numeroDeCredito" widget-class="form-control"/>
			<f:field property="tipo" widget-class="form-control chosen-select"/>
			<f:field property="cuotaFija"  label="Descuento">
				<input id="${property}" type="text" name="${property}"  class="form-control decimal" value="${value}"/>
			</f:field>
			<f:field property="activo" input-type="text" input-class="form-control " />
			<f:field property="suspension" />
			<f:field property="comentario" widget-class="form-control"/>
		</f:with>

		<script type="text/javascript">
			$(function(){
				$(".decimal").autoNumeric('init',{vMin:'0.000'},{vMax:'9999.0000'});
			});
		</script>
		
	</content>
	
</body>
</html>