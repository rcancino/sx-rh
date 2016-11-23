<html>
<head>
	<meta charset="UTF-8">
	<title>Empleado ${empleadoInstance.id}</title>
	<meta name="layout" content="application"/>
</head>
<body>

	<div class="row wrapper border-bottom white-bg page-heading">
	    <div class="col-lg-10">
	        <h2> ${empleadoInstance.nombre}</h2>
	        <ol class="breadcrumb">
        	    <li><g:link action="generales" id="${empleadoInstance.id}">Generales</g:link></li>
        	    <li><g:link action="datosPersonales" id="${empleadoInstance.id}">Datos personales</g:link></li>
        	    <li><g:link action="perfil" id="${empleadoInstance.id}"><strong>Perfil</strong></g:link></li>
        	    <li><g:link action="salario" id="${empleadoInstance.id}">Salario</g:link></li>
        	    <li><g:link action="seguridadSocial" id="${empleadoInstance.id}">Seguridad social</g:link></li>
        	    <li><g:link action="contactos" id="${empleadoInstance.id}">Contactos</g:link></li>
        	    <li><g:link controller='pensionAlimenticia' action='edit' id="${empleadoInstance.id}">Pensión</g:link></li>
        	    <li><g:link controller='expediente' action='edit' id="${empleadoInstance.id}">Expediente</g:link></li>
	        </ol>
	        <g:if test="${empleadoInstance.status == 'BAJA'}">
	        	<div class="alert alert-danger"> BAJA ${empleadoInstance.baja.fecha.text()}</div>
	        </g:if>
	        <g:if test="${flash.message}">
	            <small><span class="label label-warning ">${flash.message}</span></small>
	        </g:if> 
	        <g:hasErrors bean="${empleadoInstance?.datosPersonales}">
	        	<div class="alert alert-danger alert-dismissable">
	        		<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
	        		<ul>
	        			<g:eachError var="err" bean="${empleadoInstance}">
	        				<li><g:message error="${err}"/></li>
	        			</g:eachError>
	        		</ul>
	        	</div>
	        </g:hasErrors>
	    </div>
	</div>

	<div class="row wrapper wrapper-content animated fadeInRight">
		<div class="col-md-12">
			<div class="ibox float-e-margins">
				<lx:iboxTitle title="Perfil de empleado"/>
				<div class="ibox-content">
					<div class="row">
						<g:render template="perfilForm"/>
					</div>
					
				</div>
				<div class="ibox-footer">
					
					<button class="btn btn-default btn-outline" data-toggle="modal" data-target="#searchForm">
						<span class="glyphicon glyphicon-search"></span> Buscar
					</button>
					<g:if test="${!edit}">
						
						<g:link class="btn btn-default btn-outline" 
							action="perfil" id="${empleadoInstance.id}" params="[edit:'true']">Modificar</g:link>
						<g:link class="btn btn-default btn-outline" action="create" > Nuevo</g:link>
						<g:if test="${empleadoInstance.status=='ALTA'}">
							<g:link class="btn btn-danger btn-outline" action="registrarBaja" id="${empleadoInstance.id }"> Registrar Baja</g:link>
						</g:if>
						<g:if test="${!empleadoInstance.status=='ALTA'}">
							<g:link class="btn btn-success" action="reingreso" id="${empleadoInstance.id}"> Reingreso</g:link>
						</g:if>
					</g:if>
					
					<div class="btn-group">
						<button type="button" name="reportes" class="btn btn-default btn-outline dropdown-toggle" 
							data-toggle="dropdown" role="menu">Reportes 
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li>
								<g:jasperReport
										jasper="CaratulaDeEmpleados"
										format="PDF"
										name="Carátula">
									<g:hiddenField name="ID" value="${empleadoInstance.id}"/>
								</g:jasperReport>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>	
		

</body>
</html>