<div class="ibox float-e-margins">
	
	<div class="ibox-title"> <h5>Fechas</h5> </div>
	
	<div class="ibox-content">
		<ul class="list-group">
			<g:each in="${vacacionesInstance.dias.sort()}" var="row">
				<li class="list-group-item"><g:formatDate date="${row}" format="dd/MM/yyyy"/>
					<g:if test="${!vacacionesInstance.autorizacion }">
						<g:link action="eliminarFecha" id="${ vacacionesInstance.id}" 
						params="[fecha:g.formatDate(date:row,format:'dd/MM/yyyy')]"> Eliminar
					</g:link>
					</g:if>
					
				</li>
			</g:each>
		</ul>
	</div>

	<div class="ibox-footer">
		<g:if test="${!vacacionesInstance.autorizacion }">
			<button id="addFecha" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#fechaForm"
				${vacacionesInstance.pg?'disabled':''}>
					Agregar fecha
			</button>
		</g:if>
	</div>
</div>

<g:render template="/_common/fechaDialog"  model="[action:'agregarFecha',row:vacacionesInstance ]"/>
