<html>
<head>
	<meta charset="UTF-8">
	<title>Empleado ${empleadoInstance.id}</title>
	<meta name="layout" content="application"/>
</head>
<body>	
	<div class="row wrapper border-bottom white-bg page-heading ">
	    <div class="col-lg-10">
	        <h2> ${empleadoInstance.nombre}</h2>
	        <g:if test="${empleadoInstance.status == 'BAJA'}">
	        	<div class="alert alert-danger"> BAJA: ${empleadoInstance.baja.fecha.text()}</div>
	        </g:if>
	        <ol class="breadcrumb">
        	    <li><g:link action="generales" id="${empleadoInstance.id}"><strong>Generales</strong></g:link></li>
        	    <li><g:link action="datosPersonales" id="${empleadoInstance.id}">Datos personales</g:link></li>
        	    <li><g:link action="perfil" id="${empleadoInstance.id}">Perfil</g:link></li>
        	    <li><g:link action="salario" id="${empleadoInstance.id}">Salario</g:link></li>
        	    <li><g:link action="seguridadSocial" id="${empleadoInstance.id}">Seguridad social</g:link></li>
        	    <li><g:link action="contactos" id="${empleadoInstance.id}">Contactos</g:link></li>
        	    <li><g:link controller='pensionAlimenticia' action='edit' id="${empleadoInstance.id}">Pensión</g:link></li>
        	    <li><g:link controller='expediente' action='edit' id="${empleadoInstance.id}">Expediente</g:link></li>
	        </ol>
	        <g:if test="${flash.message}">
	            <small><span class="label label-warning ">${flash.message}</span></small>
	        </g:if> 
	        <g:hasErrors bean="${empleadoInstance}">
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
				<lx:iboxTitle title="Datos genrales"/>
				<div class="ibox-content">
					<div class="row">
						<form class="form-horizontal" method="post">
							<g:hiddenField name="id" value="${empleadoInstance.id}"/>
							<g:hiddenField name="version" value="${empleadoInstance?.version}" />
							
							<div class="col-md-6">
							
								<fieldset ${!edit?'disabled':''}>
									<f:with bean="empleadoInstance">
										<f:field property="apellidoPaterno" widget-class="form-control" label="Apellido P."/>	
										<f:field property="apellidoMaterno" widget-class="form-control" label="Apellido M."/>	
										<f:field property="nombres" widget-class="form-control" />	
										<f:field property="fechaDeNacimiento" widget-class="form-control" label="F.Nacimiento"/>	
										<f:field property="sexo" widget-class="form-control" />	
										<f:field property="activo" widget-class="form-control" />
										<f:field property="controlDeAsistencia" widget-class="form-control" />
									</f:with>
								</fieldset>
							
							</div>
							
							<div class="col-md-6">
							
								<fieldset ${!edit?'disabled=""':''}>
									<f:with bean="empleadoInstance">
											
										<f:field property="curp" widget-class="form-control" widget-autocomplete="off"/>	
										<f:field property="rfc" widget-class="form-control" widget-autocomplete="off"/>	
										<f:field property="clave" widget-class="form-control" widget-autocomplete="off"/>	
										<f:field property="status" widget-class="form-control" />
										<f:field property="alta" widget-class="form-control" />
											
									</f:with>
								</fieldset>
								
								
								
								<fieldset disabled>
									
									<f:with bean="${empleadoInstance}">
										<f:field property="baja.fecha"  widget-class="form-control" label="Fecha B.">
											<g:field type="text" name="baja.fecha" id="bajaFecha" class="form-control" 
												value="${g.formatDate(date:empleadoInstance?.baja?.fecha,format:'dd/MM/yyyy') }"/>
										</f:field>	
										<f:field property="baja.motivo" widget-class="form-control" />
										<f:field property="baja.causa" widget-class="form-control" />
										<f:field property="baja.comentario" widget-class="form-control" label="Comentario"/>	
									</f:with>

								</fieldset>
							</div>
						
							<g:if test="${edit}">
								<div class="form-group">
						    		<div class="col-sm-offset-8 col-sm-12">
						      			<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
						      			<g:link class="btn btn-default" action="generales" id="${empleadoInstance.id}" >Cancelar</g:link>
						    		</div>
								</div>
							</g:if>
						</form>	
					</div>
					
					
					
				</div>
				<div class="ibox-footer">
					
					<lx:searchEmpleado/>
					<g:if test="${!edit}">
						<g:link class="btn btn-default btn-outline" action="generales" id="${empleadoInstance.id}" params="[edit:'true']">Modificar</g:link>
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