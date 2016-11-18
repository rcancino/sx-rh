<g:set var="reportesControllers" 
    value="${['reportes']}" />
<li class="${reportesControllers.contains(webRequest.controllerName)?'active':''}">
    <a href="#">
    	<i class="fa fa-line-chart"></i> 
    	<span class="nav-label"> Reportes</span>
    	<span class="fa arrow"></span>
    </a>
    <ul class="nav nav-second-level collapse">
        
        <li class="${webRequest.controllerName=='reportes'?'active':''}">
            <g:link controller="reportes">
                <span class="nav-label">Impuestos sobre nómina</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>
        
    </ul>
</li>