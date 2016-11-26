<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<title>Alta de empleado</title>
	<g:set var="entity" value="${empleadoInstance}" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

<content tag="header">
	Alta de empleado
</content>

<content tag="formFields">
	<f:with bean="empleadoInstance">
		<f:field property="apellidoPaterno" widget-class="form-control" wrapper="bootstrap3"/>	
		<f:field property="apellidoMaterno" widget-class="form-control" wrapper="bootstrap3"/>	
		<f:field property="nombres" widget-class="form-control" wrapper="bootstrap3"/>	
		<f:field property="fechaDeNacimiento" widget-class="form-control" label="F.Nacimiento" wrapper="bootstrap3"/>	
		<f:field property="sexo" widget-class="form-control" wrapper="bootstrap3"/>	
		<f:field property="curp" widget-class="form-control" wrapper="bootstrap3"/>	
		<f:field property="rfc" widget-class="form-control" wrapper="bootstrap3"/>	
		<f:field property="clave" widget-class="form-control" wrapper="bootstrap3"/>	
		<f:field property="alta" widget-class="form-control" wrapper="bootstrap3"/>	
		<f:field property="activo" widget-class="form-control" wrapper="bootstrap3"/>
	</f:with>
</content>
	
</body>
</html>



%{-- 	
	
</body>
</html>



<html>
<head>
	<meta charset="UTF-8">
	<title>Alta de empleado</title>
</head>
<body>

<content tag="title">
	Alta de empleado
</content>

<content tag="content">
	<form class="form-horizontal" method="post">
		
		<div class="col-md-6">
			<fieldset >
				<f:with bean="${empleadoInstance}">
					
				</f:with>
			</fieldset>
		</div>
		
		<div class="col-md-6">
		
			<fieldset >
				<f:with bean="${empleadoInstance}">
						
					
				</f:with>
			</fieldset>
			
			
			
			
			
		</div>
	
	
	<div class="form-group">
    	<div class="col-sm-offset-8 col-sm-12">
      		<g:actionSubmit class="btn btn-primary"  value="Salvar" action="save"/>
      		<g:link class="btn btn-default" action="generales" id="${empleadoInstance.id}" >Cancelar</g:link>
    	</div>
	</div>
	

	

	</form>
</content>

</body>
</html> --}%