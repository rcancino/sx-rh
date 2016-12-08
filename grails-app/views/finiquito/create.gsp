<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<g:set var="entity" value="${finiquitoInstance}" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<title>Alta de finiquito</title>
</head>
<body>

	<content tag="header">
		Alta de finiquito
	</content>
	
	
	
	<content tag="formFields">
		<f:with bean="${finiquitoInstance }">
			<f:field property="empleado" widget-class="form-control"/>
			<f:field property="comentario" widget-class="form-control"/>
		</f:with>

		<script type="text/javascript">
			$(function(){
				$("#tipo").on('change',function(e){
					if ($(this).val()==='IMPORTE_FIJO'){
						$("[name='tasaDescuento']").prop('disabled', true).val(0.0);
						$("[name='importeFijo']").prop('disabled', false);
					}else{
						$("[name='tasaDescuento']").prop('disabled', false);
						$("[name='importeFijo']").prop('disabled', true).val(0.0);
					}
					console.log('Tipo de de finiquito: '+$(this).val());
				});
			});
		</script>
	</content>

	
	
	
	
</body>
</html>