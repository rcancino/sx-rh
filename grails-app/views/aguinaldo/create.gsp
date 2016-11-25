<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<title>Alta de aguinaldo</title>
	<g:set var="entity" value="${aguinaldoInstance}" scope="request" />
	<g:set var="entityName" value="Calculo de Aguinaldo" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

	<content tag="header">
		Alta de aguinaldo
	</content>

	<content tag="formTitle">
		Empleado
	</content>
	<content tag="formFields">
		<f:with bean="${aguinaldoInstance }">
				<f:field property="ejercicio" widget-class="form-control"/>
				<f:field property="empleado" widget-class="form-control"/>
			</f:with>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="index">
  					<span class="glyphicon glyphicon-arrow-left"></span> Lista de aguinaldos
  			    </g:link>
  			</li>
  			<li><g:link action="create">
  					<span class="glyphicon glyphicon-plus"></span> Nuevo
  			    </g:link>
  			</li>
		</ul>
	</content>
	
	
	<content tag="form">
		
		<g:hasErrors bean="${aguinaldoInstance}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${aguinaldoInstance}" as="list" />
            </div>
        </g:hasErrors>
		
		<g:form class="form-horizontal" action="save">
			
			<f:with bean="${aguinaldoInstance }">
				<f:field property="ejercicio" input-class="form-control"/>
				<f:field property="empleado" input-class="form-control"/>
			</f:with>
			<div class="form-group">
		    	<div class="col-sm-offset-9 col-sm-2">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-floppy-save"></span> Guardar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
		
		<r:script>
			
		</r:script>

	</content>
	
</body>
</html>