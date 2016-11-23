<form  class="form-horizontal numeric-form" method="post">
	<g:hiddenField name="id" value="${empleadoInstance.id}"/>
	<g:hiddenField name="version" value="${empleadoInstance?.version}" />
	
	<div class="col-md-10">
		<fieldset ${!edit?'disabled=""':''}>
			<f:with bean="empleadoInstance">
				<f:field property="contacto.nombre" widget-class="form-control" />	
				<f:field property="contacto.parentesco" widget-class="form-control" />
				<f:field property="contacto.telefono1" widget-class="form-control" />
				<f:field property="contacto.telefono2" widget-class="form-control" />
				%{-- <f:field property="contacto.direccion"  /> --}%
				<g:render template="/common/direccion" bean="${empleadoInstance.contacto}"/>
			</f:with>
		</fieldset>
	</div>

	<g:if test="${edit}">
		<div class="form-group">
    		<div class="col-sm-offset-8 col-sm-12">
      			<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
      			<g:link class="btn btn-default" action="contactos" id="${empleadoInstance.id}" >Cancelar</g:link>
    		</div>
		</div>
	</g:if>

</form>