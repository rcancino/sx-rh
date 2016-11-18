<form  class="form-horizontal" method="post" >
	<g:hiddenField name="id" value="${empleadoInstance.id}"/>
	<g:hiddenField name="version" value="${empleadoInstance?.version}" />
	<g:hiddenField name="view" value="datosPersonales" />
							
	<div class="col-md-6">
	
		<fieldset ${!edit?'disabled=""':''}>
			<f:with bean="empleadoInstance">
				<f:field property="datosPersonales.lugarDeNacimiento" widget-class="form-control" wrapper="bootstrap3"/>	
				<f:field property="datosPersonales.telefono1" widget-class="form-control" wrapper="bootstrap3"/>
				<f:field property="datosPersonales.telefono2" widget-class="form-control" wrapper="bootstrap3"/>
				<f:field property="datosPersonales.email" widget-class="form-control" wrapper="bootstrap3"/>
				<f:field property="datosPersonales.tipoDeSangre" widget-class="form-control" wrapper="bootstrap3"/>
				<f:field property="datosPersonales.estadoCivil" widget-class="form-control" wrapper="bootstrap3"/>
				<f:field property="datosPersonales.conyuge" label="Conyuge" widget-class="form-control" wrapper="bootstrap3"/>
				
				<f:field property="datosPersonales.nombreDelPader" widget-class="form-control" label="Nombre del padre" wrapper="bootstrap3"/>
				
				<f:field property="datosPersonales.nombreDeLaMadre" widget-class="form-control" wrapper="bootstrap3"/>
					
			</f:with>
		</fieldset>
	
	</div>
	<div class="col-md-6">
	
		<fieldset ${!edit?'disabled=""':''}>
			<f:with bean="empleadoInstance">
				<f:field property="datosPersonales.direccion.calle" widget-class="form-control" wrapper="bootstrap3"/>	
				<f:field property="datosPersonales.direccion.numeroExterior" widget-class="form-control" wrapper="bootstrap3"/>
				<f:field property="datosPersonales.direccion.numeroInterior" widget-class="form-control" wrapper="bootstrap3"/>
				<f:field property="datosPersonales.direccion.colonia" widget-class="form-control" wrapper="bootstrap3"/>
				<f:field property="datosPersonales.direccion.municipio" widget-class="form-control" wrapper="bootstrap3"/>
				<f:field property="datosPersonales.direccion.estado" widget-class="form-control" wrapper="bootstrap3"/>
				<f:field property="datosPersonales.direccion.codigoPostal" widget-class="form-control" wrapper="bootstrap3"/>
				<f:field property="datosPersonales.direccion.pais" widget-class="form-control" wrapper="bootstrap3"/>
			</f:with>
		</fieldset>
	
	</div>

	<g:if test="${edit}">
		<div class="form-group">
	    	<div class="col-sm-offset-8 col-sm-12">
	      		<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
	      		<g:link class="btn btn-default" action="datosPersonales" id="${empleadoInstance.id}" >Cancelar</g:link>
	    	</div>
		</div>
	</g:if>
</form>