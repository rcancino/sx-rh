<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<g:set var="entity" value="${prestamoInstance}" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<title>Alta de prestamo</title>
</head>
<body>

	<content tag="header">
		Alta de prestamo
	</content>
	
	
	%{-- <content tag="formTitle">Prestamo nuevo</content> --}%
	<content tag="formFields">
		<f:with bean="${prestamoInstance }">
			<f:field property="empleado" widget-class="form-control"/>
			<f:field property="alta" widget-class="form-control"/>
			<f:field property="autorizo" widget-class="form-control"/>
			<f:field property="fechaDeAutorizacion" widget-class="form-control"/>
			<f:field property="importe" widget="numeric"/>
			<f:field property="tipo" widget-class="form-control chosen-select"/>
			<f:field property="tasaDescuento" widget="porcentaje"/>
			<f:display property="importeFijo" widget-class="form-control" />
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
					console.log('Tipo de de prestamo: '+$(this).val());
				});
			});
		</script>
	</content>

	
	
	
	
</body>
</html>