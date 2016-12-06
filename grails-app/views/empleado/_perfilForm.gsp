<form class="form-horizontal" method="post">
	<g:hiddenField name="id" value="${empleadoInstance.id}"/>
	<g:hiddenField name="version" value="${empleadoInstance?.version}" />
	<g:hiddenField name="view" value="perfil" />
		<div class="col-md-8">
			
				<fieldset ${!edit?'disabled=""':''}>
					<f:with bean="empleadoInstance">
						<f:field property="perfil.tipo" widget-class="form-control" wrapper="bootstrap3"/>	
						<f:field property="perfil.numeroDeTrabajador" widget-class="form-control" wrapper="bootstrap3"/>	
						<f:field property="perfil.puesto" widget-class="form-control" wrapper="bootstrap3"/>	
						<f:field property="perfil.departamento" widget-class="form-control" wrapper="bootstrap3"/>	
						<f:field property="perfil.ubicacion" widget-class="form-control" wrapper="bootstrap3"/>	
						<f:field property="perfil.tipoDeContrato" widget-class="form-control" wrapper="bootstrap3"/>	
						<f:field property="perfil.jornada" widget-class="form-control" wrapper="bootstrap3"/>	
						
						<f:field property="perfil.regimenContratacion" widget-class="form-control" wrapper="bootstrap3"
							value="${value?:com.luxsoft.sw4.rh.sat.SatRegimenContratacion.findByClave(2) }" />
						<f:field property="perfil.riesgoPuesto" widget-class="form-control"
							value="${value?:com.luxsoft.sw4.rh.sat.SatRiesgoPuesto.findByClave(3) }" wrapper="bootstrap3"/>
						<f:field property="perfil.turno" widget-class="form-control" wrapper="bootstrap3"/>
						<f:field property="perfil.tipoDeIncentivo" widget-class="form-control" wrapper="bootstrap3"/>
						<f:field property="perfil.declaracionAnual" label="Presenta Declaracion" widget-class="form-control" />
					</f:with>
				</fieldset>
		</div>
		
		<g:if test="${edit}">
			<div class="form-group">
	    		<div class="col-sm-offset-8 col-sm-12">
	      			<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
	      			<g:link class="btn btn-default" action="perfil" id="${empleadoInstance.id}" >Cancelar</g:link>
	    		</div>
			</div>
		</g:if>

</form>