package br.gov.jfrj.siga.vraptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;

@Resource
public class IdentidadeController extends GiControllerSupport {

	private DpPessoaSelecao selecaoPessoa;
	
	public IdentidadeController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
		selecaoPessoa = new DpPessoaSelecao();
	}
	
	@Get("/app/gi/identidade/listar")
	public void lista(DpPessoaSelecao pessoaSel) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		DpPessoa pes = definirPessoa(pessoaSel);

		if (pes != null) {
			result.include("itens", dao().consultaIdentidades(pes));
		}
		
		result.include("pessoaSel", selecaoPessoa);
	}
	
	@Get("/app/gi/identidade/editar_gravar")
	public void aEditarGravar(String dtExpiracao, Long id) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		if (id == null)
			throw new AplicacaoException("N�o foi informada id");

		Date dataExpiracao = null;
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		try {
			dataExpiracao = df.parse(dtExpiracao + " 00:00");
		} catch (final ParseException e) {
		} catch (final NullPointerException e) {
		}

		CpIdentidade ident = daoId(id);
		Cp.getInstance()
			.getBL()
			.alterarIdentidade(ident, dataExpiracao, getIdentidadeCadastrante());
		
		result.forwardTo(this).lista(selecaoPessoa);
	}

	@Get("/siga/app/gi/identidade/cancelar")
	public void aCancelar(Long id) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		if (id == null)
			throw new AplicacaoException("N�o foi informada id");

		CpIdentidade ident = daoId(id);
		Cp.getInstance().getBL().cancelarIdentidade(ident,
				getIdentidadeCadastrante());
	}

	@Get("/siga/app/gi/identidade/desbloquear")
	public void aBloquear(Long id) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		if (id != null) {
			CpIdentidade ident = daoId(id);
			Cp.getInstance().getBL()
				.bloquearIdentidade(ident, getIdentidadeCadastrante(), true);
		} else
			throw new AplicacaoException("N�o foi informada id");
	}

	@Get("/siga/app/gi/identidade/bloquear")
	public void aDesbloquear(Long id) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		if (id != null) {
			CpIdentidade ident = daoId(id);
			Cp.getInstance().getBL().bloquearIdentidade(ident,
					getIdentidadeCadastrante(), false);
		} else
			throw new AplicacaoException("N�o foi informada id");
	}

	@Get("/siga/app/gi/identidade/bloquear_pessoa")
	public void aBloquearPessoa(DpPessoaSelecao pessoaSel) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		DpPessoa pes = definirPessoa(pessoaSel);

		if (pes != null) {
			Cp.getInstance().getBL().bloquearPessoa(pes,
					getIdentidadeCadastrante(), true);
		} else
			throw new AplicacaoException("N�o foi informada a pessoa");
	}

	@Get("/siga/app/gi/identidade/desbloquear_pessoa")
	public void aDesbloquearPessoa(DpPessoaSelecao pessoaSel) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		DpPessoa pes = definirPessoa(pessoaSel);

		if (pes != null) {
			Cp.getInstance().getBL().bloquearPessoa(pes,
					getIdentidadeCadastrante(), false);
		} else
			throw new AplicacaoException("N�o foi informada a pessoa");
	}
	
	public CpIdentidade daoId(long id) {
		return dao().consultar(id, CpIdentidade.class, false);
	}
	
	private DpPessoa definirPessoa(DpPessoaSelecao pessoaSel) {
		DpPessoa pessoa = null;
		
		if (pessoaSel != null) {
			pessoa = pessoaSel.buscarObjeto();
			selecaoPessoa = pessoaSel;
		}
		
		return pessoa;
	}
	
}