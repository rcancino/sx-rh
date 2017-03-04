<form  class="form-horizontal " method="post">
	<g:hiddenField name="id" value="${empleadoInstance.id}"/>
	<g:hiddenField name="version" value="${empleadoInstance?.version}" />
	<g:hiddenField name="view" value="salario" />
	<div class="col-md-6">
		<fieldset ${!edit?'disabled=""':''}>
			<f:with bean="empleadoInstance">
				<f:field widget-id="periodicidadField" property="salario.periodicidad" widget-class="form-control" wrapper="bootstrap3"/>	
				<g:if test="${empleadoInstance?.salario?.salarioDiario}">
					<f:field property="salario.salarioDiario" 
						widget-id="salarioNuevo" 
						widget-class="form-control numeric" 
						wrapper="bootstrap3"/>
				</g:if>
				<g:else>
					<f:field property="salario.salarioDiario" wrapper="bootstrap3">
						<input type="text" name="salario.salarioDiario" class="form-control numeric" id="salarioNuevo" value="${value}" >
					</f:field>
					%{-- <f:field property="salario.salarioDiario" 
						widget-id="salarioNuevo" 
						widget-class="form-control numeric" 
						wrapper="bootstrap3"/> --}%
					<f:field property="salario.salarioVariable" widget-type="text"
						widget-id="salarioVariable"
						widget-class="form-control numeric" 
						wrapper="bootstrap3"/>	
				</g:else>
				
				<f:field property="salario.salarioDiarioIntegrado" widget-id="sdiNuevo" label="SDI"
						widget-class="form-control " 
						widget-readonly="true" 
						wrapper="bootstrap3"/>	

				<f:field property="salario.formaDePago" widget-class="form-control " wrapper="bootstrap3"/>	

				<f:field property="salario.clabe" widget-class="form-control" input-autocomplete="off" label="CLABE" wrapper="bootstrap3"/>
				<f:field property="salario.numeroDeCuenta" widget-class="form-control" input-autocomplete="off" wrapper="bootstrap3"/>	
				<f:field property="salario.banco" widget-class="form-control" wrapper="bootstrap3"/>	
				<f:field property="salario.primaDominicalFija" widget-class="form-control" wrapper="bootstrap3" label="Prima Dom f."/>	
			</f:with>
		</fieldset>
	</div>

	<g:if test="${edit}">
		<div class="form-group">
			<div class="col-sm-offset-8 col-sm-12">
				<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="updateSalario"/>
				<g:link class="btn btn-default" action="salario" id="${empleadoInstance.id}" >Cancelar</g:link>
			</div>
		</div>
	</g:if>

</form>