<form class="form-horizontal" method="post">
		<g:hiddenField name="id" value="${empleadoInstance.id}"/>
		<g:hiddenField name="version" value="${empleadoInstance?.version}" />
		<g:hiddenField name="view" value="seguridadSocial" />
		<div class="col-md-6">
		
			<fieldset ${!edit?'disabled=""':''}>
				<f:with bean="empleadoInstance">
					<f:field property="seguridadSocial.numero" widget-class="form-control" label="Número IMSS" wrapper="bootstrap3"/>
					<f:field property="seguridadSocial.turno" widget-class="form-control" wrapper="bootstrap3"/>	
					<f:field property="seguridadSocial.unidadMedica" widget-class="form-control" wrapper="bootstrap3"/>	
					<f:field property="seguridadSocial.comentario" widget-class="form-control" wrapper="bootstrap3"/>	
				</f:with>
			</fieldset>
		
		</div>
		
	
	<g:if test="${edit}">
	<div class="form-group">
    	<div class="col-sm-offset-8 col-sm-12">
      		<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
      		<g:link class="btn btn-default" action="seguridadSocial" id="${empleadoInstance.id}" >Cancelar</g:link>
    	</div>
	</div>
	</g:if>

	

	</form>