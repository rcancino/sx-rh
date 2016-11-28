<html>
<head>
	<meta charset="UTF-8">
	<title>Alta de periodo</title>
</head>
<body>

<lx:header>
	<h3>Alta de periodo al calendario: </h3>
	<small>${calendarioInstance}</small>
	<lx:errorsHeader bean="${calendarioDetInstance}"/>
</lx:header>

<div class=" row wrapper wrapper-content   animated fadeInRight">
	<div class="col-md-8 col-md-offset-2">
		<g:form name="createForm" action="agregarPeriodo" class="form-horizontal" method="POST" id="${calendarioInstance.id}">  
			<g:hiddenField name="folio" value="${calendarioDetInstance.folio}"/>
			
			<g:hiddenField name="version" value="${calendarioInstance.version}"/>
			<lx:ibox>
				<lx:iboxTitle title="Propiedades"/>
				<lx:iboxContent>
				<f:with bean="${calendarioDetInstance}">
					
					<f:display property='folio' />

					<f:field property='mes' input-class="form-control">
					  <g:select id="mesField" class="form-control"  
		                  name="mes" 
		                  value="${calendarioDetInstance.mes}"
		                  noSelection="['':'-Seleccione un mes-']"
		                  from="${com.luxsoft.sw4.Mes.getMeses().collect{it.nombre}}"/>
					</f:field>

					<f:field property='bimestre' input-class="form-control">
						<g:select id="mesField" class="form-control"  
		                	name="bimestre" 
		                	value="${calendarioDetInstance.bimestre}"
		                	noSelection="['':'-Seleccione un bimestre-']"
		                	from="${(1..6)}"/>
					</f:field>
					<f:field property='inicio' input-class="form-control"/>
					<f:field property='fin' input-class="form-control"/>
					<f:field property='fechaDePago' input-class="form-control" label="Pago"/>
					<fieldset>
					    <legend><small>Asistencia:</small></legend>
					    <f:field property='asistencia.fechaInicial' input-class="form-control"/>
					    <f:field property='asistencia.fechaFinal' input-class="form-control"/>
					</fieldset>
				</f:with>
				</lx:iboxContent>
				<lx:iboxFooter>
					<div class="btn-group">
					    <button id="saveBtn" class="btn btn-primary ">
					        <i class="fa fa-floppy-o"></i> Salvar
					    </button>
					    <lx:backButton action="edit" id="${calendarioInstance.id}" />
					</div>
				</lx:iboxFooter>
			</lx:ibox>

		</g:form>
		
	</div>
</div>
<script type="text/javascript">
    $(function(){
        $('.date').bootstrapDP({
            format: 'dd/mm/yyyy',
            keyboardNavigation: false,
            forceParse: false,
            autoclose: true,
            todayHighlight: true
        });
        
        $('form[name=createForm]').submit(function(e){
            console.log("Desablidatndo submit button....");

            var button=$("#saveBtn");
            button.attr('disabled','disabled')
            .html('Procesando...');

            $(".money,.porcentaje,.numeric,.tc",this).each(function(index,element){
              var val=$(element).val();
              var name=$(this).attr('name');
              var newVal=$(this).autoNumeric('get');
              $(this).val(newVal);
            });
            //e.preventDefault(); 
            return true;
        });

    });
</script> 

	
</body>
</html>


