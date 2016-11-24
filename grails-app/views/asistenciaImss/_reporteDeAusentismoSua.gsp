<div class="modal fade" id="reporteDeAusentismoSua" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Ausentismo Sua</h4>
			</div>
				<g:jasperReport
					format="PDF" name=""
         						 jasper="AusentismoSua"
         						 buttonPosition="bottom"
         						 
         						 class="form-horizontal">
				<div class="modal-body">
					<div class="row">
					<div class="form-group">
    					<label for="calendarioIni" class="col-sm-3">Fecha Inicio</label>
    					<div class="col-sm-9">
							<g:datePicker id="inicioPago" name="FECHA_INI"  precision="day" class="form-control"/>
    					</div>
  					</div>
					
					<div class="form-group">
    					<label for="calendarioIni" class="col-sm-3">Fecha fin</label>
    					<div class="col-sm-9">
							<g:datePicker id="inicioPago" name="FECHA_FIN"  precision="day" class="form-control"/>	
    					</div>
  					</div>
  					</div>
				</div>
				
				</g:jasperReport>


		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
