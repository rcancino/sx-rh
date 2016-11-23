<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<g:set var="entity" value="${fonacotInstance}" scope="request" />
	<g:set var="editable" value="${false}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	<title>Alta de prestamo FONACOT</title>
</head>
<body>

<content tag="header">
	Alta de prestamo FONACOT
</content>

<content tag="formTitle">Crédito FONACOT</content>

<content tag="formFields">
	<f:with bean="${fonacotInstance }">
		<f:field property="empleado" />
		<f:field property="numeroDeCredito" widget-class="form-control"/>
		<f:field property="numeroDeFonacot" widget-class="form-control"/>
		<f:field property="importe" widget="money"/>
		<f:field property="retencionMensual" widget="money"/>
		
	</f:with>
</content>

	
	
	
	
	
	<content tag="form">
		
		<g:hasErrors bean="${fonacotInstance}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${fonacotInstance}" as="list" />
            </div>
        </g:hasErrors>
		
		<g:form class="form-horizontal" action="save">
			
			
			<div class="form-group">
		    	<div class="col-sm-offset-9 col-sm-2">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-floppy-save"></span> Guardar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
		
		

	</content>
	
</body>
</html>