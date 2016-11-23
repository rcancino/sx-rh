<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="application"/>
	<title>Baja de empleado</title>
</head>
<body>

<lx:header>
	<h2>Baja de empleado: ${empleadoInstance }</h2>
</lx:header>

<div class=" row wrapper wrapper-content animated fadeInRight">
	<div class="col-md-6 col-md-offset-3">
		<lx:ibox>
			<g:form class="form-horizontal numeric-form" action="registrarBaja">
				<g:hiddenField name="empleado.id" value="${empleadoInstance.id}"/>
				
				<lx:iboxTitle title="Propiedades"/>

				<lx:iboxContent>
					<f:with bean="bajaInstance">
						<f:field property="fecha" />	
						<f:field property="motivo" widget-class="form-control"/>
						<f:field property="causa" widget-class="form-control"/>
						<f:field property="comentario" widget-class="form-control"/>
					</f:with>
				</lx:iboxContent>
				
				<lx:iboxFooter>
					<div class="btn-group">
						<button type="submit" class="btn btn-danger">
							<i class="fa fa-ban"></i> Registrar
						</button>
						<g:link class="btn btn-default" action="generales" id="${empleadoInstance.id}" >Cancelar</g:link>
					</div>
				</lx:iboxFooter>			
			</g:form>
		</lx:ibox>
	</div>
	
</div>

<script type="text/javascript">
	$(function(){
		$('.date').bootstrapDP({
		    format: 'dd/mm/yyyy',
		    keyboardNavigation: false,
		    forceParse: false,
		    autoclose: true,
		    todayHighlight: true,

		});

	});
</script>
	
</body>
</html>