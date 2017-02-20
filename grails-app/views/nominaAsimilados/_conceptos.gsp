<%@page expressionCodec="none" %>
<table class="table  table-bordered table-condensed">
	<thead>
		<tr>
			<th>Clave</th>
			<th>Concepto</th>
			<th>Gravado</th>
			<th>Excento</th>
			
			<th></th>
			%{-- <th></th> --}%
			<g:if test="${!nominaPorEmpleadoInstance.cfdi}">
			<th></th>
			</g:if>
		</tr>
	</thead>
	<tbody>
		
		<g:findAll in="${nominaPorEmpleadoInstance?.conceptos}" expr="it.concepto.tipo==param" >
			
			<tr>
				<td>
					${fieldValue(bean:it,field:"concepto.clave")}
				</td>
				<td>
					${fieldValue(bean:it,field:"concepto.descripcion")} 
					<g:if test="${param == 'PERCEPCION'}">
						${it.parent.nomina.calendarioDet.mes.toUpperCase()} ${it.parent.nomina.ejercicio}

					</g:if>
					
				</td>
				<td class="text-right"><g:formatNumber number="${it.importeGravado}" format="#,###,###.##" minFractionDigits="2"/></td>
				<td class="text-right"><g:formatNumber number="${it.importeExcento}" format="#,###,###.##" minFractionDigits="2"/></td>
				
				<td>
					<a  data-target="#conceptoInfoDialog" data-toggle="modal"
						data-url="${g.createLink(action:'informacionDeConcepto',id:it.id)}">
						<span class="glyphicon glyphicon-info-sign"></span>
					</a>
					%{-- <a  
						data-container="body"
						data-toggle="popover"
						data-placement="right" 
						data-title="Resumen"
						data-content=" PENDIENTE "
						data-url="${g.createLink(action:'informacionDeConcepto',id:it.id)}"
						>
  						 <span class="glyphicon glyphicon-info-sign"></span>
					</a> --}%
				</td>
				%{-- <td class="text-center">
					<g:link class="disabled" action="modificarConcepto" id="${it.id}" data-toggle="tooltip"  title="Modificar concepto">
						<span class="glyphicon glyphicon-pencil"></span>
					</g:link>
					
				</td> --}%
				<g:if test="${!nominaPorEmpleadoInstance.cfdi}">
					<td class="text-center">
						<g:link action="eliminarConcepto" id="${it.id}" data-toggle="tooltip"  title="Eliminar concepto"
							onclick="return confirm('Eliminar concepto?')">
							<span class="glyphicon glyphicon-trash"></span>
						</g:link>
					</td>
				</g:if>
			</tr>
		</g:findAll>
	</tbody>
	<tfoot>
		<tr>
			<th></th>
			<th>Total</th>
			<th class="text-right"><g:formatNumber 
				number="${param=='PERCEPCION'?nominaPorEmpleadoInstance?.percepcionesGravadas:nominaPorEmpleadoInstance?.deduccionesGravadas}" format="#,###.##"/></th>
			<th class="text-right"><g:formatNumber 
				number="${param=='PERCEPCION'?nominaPorEmpleadoInstance?.percepcionesExcentas:nominaPorEmpleadoInstance?.deduccionesExcentas}" format="#,###.##"/></th>
			<g:if test="${!nominaPorEmpleadoInstance.cfdi}">
			<th></th>
			%{-- <th></th> --}%
			<th></th>
			</g:if>
		</tr>
	</tfoot>
</table>

<div class="modal fade" id="conceptoInfoDialog" tabindex="-1" >
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Detalle de concepto</h4>
			</div>
	
			<div class="modal-body">
				<div id="modalTarget">
					
				</div>
				
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
			</div>
	
				
		</div> <!-- moda-content -->
	</div> <!-- modal-dialog -->
</div> <!-- .modal  -->


<script type="text/javascript">
	$(document).ready(function(){

		// $(".modalBtn").on('click', function(e) {
		//    $modal.modal('toggle', $(this));
		// });

		$("a[data-toggle='modal']").on('click', function(e) {
		    $_clObj = $(this); //clicked object
		    $_mdlObj = $_clObj.attr('data-target'); //modal element id 
		    $($_mdlObj).on('shown.bs.modal',{ _clObj: $_clObj }, function (event) {
		           $_clObj = event.data._clObj; //$_clObj is the clicked element !!!
		           //do your stuff here...
		           var url = $_clObj.data('url'); // Extract info from data-* attributes
		           //console.log(url);
		           var modal = $(this);
					modal.find('#modalTarget').text('Cargando datos...');
					$.ajax({
						type:'GET',
						url:url,
						dataType:'html',
						success:function(data){
							modal.find('#modalTarget').html(data);
							// element.attr('data-content',data);
							// element.attr('data-popover-visible',"true");
							// element.popover('show');
						}
					});

		    });
		});
		/*
		$("#conceptoInfoDialog").on('show.bs.modal', function(event){
			var button = $(event.relatedTarget); // Button that triggered the modal
			var url = button.data('url'); // Extract info from data-* attributes
			var surtido = button.data('surtido');
			console.log(url);
			


			var modal = $(this);
			modal.find('#modalTarget').text(url);

		});
		*/
		var get_data_for_popover=function(){
			var element=$(this);
			var url=$(this).attr('data-url');
			console.log('URL: ' + url);
			return "DEMO DATA"
			/*
			if($(this).attr('data-popover-visible')==="true"){
				
				element.popover('hide');
				element.attr('data-popover-visible',"false");
				return;
			}
			$.ajax({
				type:'GET',
				url:url,
				dataType:'html',
				success:function(data){
					element.attr('data-content',data);
					element.attr('data-popover-visible',"true");
					element.popover('show');
				}
			});
			*/
		}
		
		//$('[data-toggle="popover"]').popover({container:'body', html: true, content:'HOLA'});
		//$('[data-toggle="popover"]').click(get_data_for_popover);
		
		//$('[data-popover=true]').popover({"trigger":"manual","html":"true"});
	});
	
	
</script>


