<div class="ibox float-e-margins">
    <div class="ibox-title"> 
        <h5>Empleados</h5> 
        <div class="ibox-tools">
            <g:link action="selectorDeEmpleados" id="${operacionGenericaGrupoInstance.id}">
                    <i class="fa fa-plus"></i> 
            </g:link>
            <a class="collapse-link">
                <i class="fa fa-chevron-up"></i>
            </a>
            <a class="close-link">
                <i class="fa fa-times"></i>
            </a>
        </div>
    </div>
    <div class="ibox-content">
        <table id="empleadosGrid" class="table table-striped table-bordered table-condensed">
            <thead>
                <tr>
                    <th>Folio</th>
                    <th>Empleado</th>
                    <th>Ubicación</th>
                    <g:if test="${editable == true}">
                        <th>E</th>
                    </g:if>
                    
                </tr>
            </thead>
            <tbody>
                <g:each in="${operacionGenericaGrupoInstance.partidas}" var="row">
                    <tr >
                        <td>
                            <g:link controller="operacionGenerica" action="show" id="${row.id}">
                                ${g.formatNumber(number:row.id, format:'#####')}    
                            </g:link>
                        </td>
                        <td>${fieldValue(bean:row,field:"empleado.nombre")}</td>
                        <td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
                        <g:if test="${editable == true}">
                            <td>
                                <g:link onclick="confirm('Eliminar operación genérica para el empleado');"
                                    action="eleiminarPartida" 
                                    id="${operacionGenericaGrupoInstance.id}" params="[operacionId:row.id]">
                                    <i class="fa fa-trash"></i>
                                </g:link>
                            </td>
                        </g:if>
                        
                    </tr>
                </g:each>
            </tbody>
        </table>
    </div>
</div>

<script type="text/javascript">
    $(function(){
        $('#empleadosGrid').dataTable({
            responsive: true,
            aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
            "language": {
                "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
            },
            "dom": 'lfrtip',
            "tableTools": {
                "sSwfPath": "${assetPath(src: 'plugins/dataTables/swf/copy_csv_xls_pdf.swf')}"
            },
            "order": []
        });
        var table = $('#grid').DataTable();
        $("#filtro").on('keyup',function(e){
            var term=$(this).val();
            table.search(term).draw();
        });
    });
</script>
