<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ attribute name="sigla" %>
<%@ attribute name="descricao" %>
<%@ attribute name="pessoaParam" %>
<%@ attribute name="lotacaoParam" %>
<%@ attribute name="isVraptor" %>

<c:if test="${empty sigla}">
<c:set var="sigla" value="&nbsp;" />
</c:if>

<c:choose>
	<c:when test="${not empty isVraptor}">
		<c:if test="${not empty pessoaParam}">
		       <a href="/app/siga/pessoa/exibir?sigla=${pessoaParam}">
			       <span ${estilo} title="${descricao}">
			       		${sigla}
			       </span>
		       </a>
		</c:if>
		<c:if test="${not empty lotacaoParam}">
			<a href="/app/siga/lotacao/exibir?sigla=${lotacaoParam}">
				<span ${estilo} title="${descricao}">
		       		${sigla}
				</span>
			</a>
		</c:if>
		
		<c:if test="${empty pessoaParam && empty lotacaoParam}">
			<span ${estilo} title="${descricao}">
				${sigla}
			</span>
		</c:if>
	</c:when>
	<c:otherwise>
		<c:if test="${not empty pessoaParam}">
		       <a href="/siga/pessoa/exibir.action?sigla=${pessoaParam}">
			       <span ${estilo} title="${descricao}">
			       		${sigla}
			       </span>
		       </a>
		</c:if>
		
		<c:if test="${not empty lotacaoParam}">
			<a href="/siga/lotacao/exibir.action?sigla=${lotacaoParam}">
				<span ${estilo} title="${descricao}">
		       		${sigla}
				</span>
			</a>
		</c:if>
		
		<c:if test="${empty pessoaParam && empty lotacaoParam}">
			<span ${estilo} title="${descricao}">
				${sigla}
			</span>
		</c:if>
	</c:otherwise>
</c:choose>