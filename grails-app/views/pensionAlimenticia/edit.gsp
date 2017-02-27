<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="application"/>
	<asset:javascript src="forms/forms.js"/>
	
</head>
<body>


<div class="row wrapper border-bottom white-bg page-heading">
	    <div class="col-lg-10">
	        <h2> ${empleadoInstance.nombre}</h2>
	        <ol class="breadcrumb">
        	    <li><g:link controller='empleado' action="generales" id="${empleadoInstance.id}">Generales</g:link></li>
        	    <li><g:link controller='empleado' action="datosPersonales" id="${empleadoInstance.id}">Datos personales</g:link></li>
        	    <li><g:link controller='empleado' action="perfil" id="${empleadoInstance.id}">Perfil</g:link></li>
        	    <li><g:link controller='empleado' action="salario" id="${empleadoInstance.id}">Salario</g:link></li>
        	    <li><g:link controller='empleado' action="seguridadSocial" id="${empleadoInstance.id}">Seguridad social</g:link></li>
        	    <li><g:link controller='empleado' action="contactos" id="${empleadoInstance.id}">Contactos</g:link></li>
        	    <li><g:link controller='pensionAlimenticia' action='edit' id="${empleadoInstance.id}"><strong>Pensión</strong></g:link></li>
        	    <li><g:link controller='expediente' action='edit' id="${empleadoInstance.id}">Expediente</g:link></li>
	        </ol>
	        <g:if test="${empleadoInstance.status == 'BAJA'}">
	        	<div class="alert alert-danger"> BAJA ${empleadoInstance.baja.fecha}</div>
	        </g:if>
	        <g:if test="${flash.message}">
	            <small><span class="label label-warning ">${flash.message}</span></small>
	        </g:if> 
	        <lx:errorsHeader bean="${empleadoInstance}"/>
	        
	    </div>
	</div>

<div class="row wrapper wrapper-content animated fadeInRight">
	<div class="col-md-12">
		<lx:ibox>
				<g:form  class="form-horizontal numeric-form" action="actualizar">
						<lx:iboxTitle title="Pension Alimenticia"/>
						
						<g:if test="${pensionInstance}">
							<lx:iboxContent>
							<f:with bean="pensionInstance">
				
								<g:hiddenField name="id" value="${pensionInstance.id}"/>
								<g:hiddenField name="version" value="${pensionInstance.version}"/>
								<g:hiddenField name="empleado" value="${empleadoInstance.id}"/>
								<f:field property="porcentaje" widget-class="form-control" input-type="text" input-class="number"/>
								<f:field property="neto" widget-class="form-control" />
								<f:field property="beneficiario" widget-class="form-control" />
								<f:field property="comentario" widget-class="form-control" />
				
							</f:with>
						</lx:iboxContent>
						</g:if>
						<lx:iboxFooter>
							<g:if test="${pensionInstance==null}">
								<g:link class="btn btn-primary" action="create" id="${empleadoInstance.id}" >Registrar</g:link>
							</g:if>
							<g:else>
								<button type="submit" class="btn btn-primary">
									 Actualizar 
								</button>
							</g:else>
						</lx:iboxFooter>	
						
				</g:form>
		</lx:ibox>		
	</div>
</div>

		

</body>
</html>