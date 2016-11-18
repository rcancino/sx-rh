<html>
<head>
	<meta charset="UTF-8">
	<title>Operacion Generica ${operacionGenericaGrupoInstance.id}</title>
</head>
<body>

	<div class="row wrapper border-bottom white-bg page-heading">
	    <div class="col-lg-10">
	        <h2>Operación genérica en grupo ${operacionGenericaGrupoInstance.id} </h2>
	        <g:if test="${flash.message}">
	            <small><span class="label label-warning ">${flash.message}</span></small>
	        </g:if> 
	        <g:if test="${flash.error}">
	            <small><span class="label label-danger ">${flash.error}</span></small>
	        </g:if> 
	    </div>
	</div>

	<div class="row wrapper wrapper-content animated fadeInRight">
		
		<div class="col-md-6">
			<div class="ibox float-e-margins">
				<g:form name="updateForm" action="update" class="form-horizontal" method="PUT">  
					<g:hiddenField name="id" value="${operacionGenericaGrupoInstance.id}"/>
					<g:hiddenField name="version" value="${operacionGenericaGrupoInstance.version}"/>

					<div class="ibox-title"> <h5>Propiedades</h5> </div>

					<div class="ibox-content">
						<f:with bean="${operacionGenericaGrupoInstance}">
							<f:display property="tipo" widget-class="form-control"/>
							<f:display property="concepto" widget-class="form-control"/>
							<f:display property="importeGravado" widget-class="form-control" input-type="text"/>
							<f:display property="importeExcento" widget-class="form-control" input-type="text"/>
							<f:display property="calendarioDet" widget-class="form-control"/>
							<f:display property="comentario" widget-class="form-control"/>
						</f:with>
					</div>
					<div class="ibox-footer">
						<div class="btn-group">
						    <lx:backButton  id="${operacionGenericaGrupoInstance.id}" />
						    <lx:editButton id="${operacionGenericaGrupoInstance.id}"/>
						</div>	
					</div>
				</g:form>
			</div>
		</div>

		<div class="col-md-6">
			<g:render template="partidasShowPanel"/>
		</div>
	</div>

	
</body>
</html>