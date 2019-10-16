package listener;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import binario.RegistroBinario;
import indices.TabelaHash;
import indices.TabelaHashBinaria;

public class ListenerPort implements Container {

	static FazAi fazAi = new FazAi();

	@Override
	public void handle(Request request, Response response) {
		try {
			String path = request.getPath().getPath();
			System.out.println("Request: " + request.getQuery().toString());
			System.out.println(path.toString());

			if (path.startsWith("/fazai")/* && "POST".equals(method) */) {
				JSONArray json = fazAi.fazONegocio(request.getQuery().get("key"));
				this.enviaResposta(Status.CREATED, response, json);

			} else {
				this.naoEncontrado(response, path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void naoEncontrado(Response response, String path) throws Exception {
		enviaResposta(Status.OK, response, new JSONArray().put("Não encontrado."));
	}

	private void enviaResposta(Status status, Response response, JSONArray json) throws Exception {
		PrintStream body = response.getPrintStream();
		long time = System.currentTimeMillis();
		response.setValue("Access-Control-Allow-Origin", "*");
		response.setValue("Content-Type", "application/json");
		response.setValue("Server", "");
		response.setDate("Date", time);
		response.setDate("Last-Modified", time);
		response.setStatus(status);
		body.println(json);
		body.close();
	}

	public static void main(String[] list) throws Exception {
		// new FazAi().recompilar();
		int porta = 1880;

		// Configura uma conexão soquete para o servidor HTTP.
		Container container = new ListenerPort();
		ContainerSocketProcessor servidor = new ContainerSocketProcessor(container);
		Connection conexao = new SocketConnection(servidor);
		SocketAddress endereco = new InetSocketAddress(porta);
		conexao.connect(endereco);

		Desktop.getDesktop().browse(new File("interface/index.html").toURI());

		System.out.println("Interromper o servidor? (y/n)");

		Scanner ler = new Scanner(System.in);
		String a = "";
		while (!a.equals("y")) {
			a = ler.next();
			if (a.equals("n"))
				System.out.println("Então não.");
			else if (a.equals("recompilar"))
				new FazAi().recompilar();
			else if (!a.equals("y") && !a.equals("n"))
				System.out.println("Sem tempo irmão.");
		}
		ler.close();
		fazAi.close();
		conexao.close();
		servidor.stop();
		System.out.println("Servidor terminado.");
	}
}

class FazAi {

	RegistroBinario bancoBinario;
	RegistroBinario indiceBinario;
	TabelaHashBinaria hashBinario;

	private static String[] structBanco = { "int", "String", "String", "String", "String", "String", "float", "float",
			"float", "float", "float" };
	private static String[] structHash = { "String", "int", "int" };

	public FazAi() {
		bancoBinario = new RegistroBinario("banco.bin", structBanco, 60);
		indiceBinario = new RegistroBinario("indice-invertido.bin", null, 60);
		;
		hashBinario = new TabelaHashBinaria("indice-hash.bin", 6373);
	}

	public JSONArray fazONegocio(String keyString) {
		Set<String[]> hashSet = new LinkedHashSet<String[]>();
		String[] keyStrings = keyString.split("[^A-Za-z]");
		for (String keyString2 : keyStrings) {
			String[] line = indiceBinario.getData(hashBinario.getData(keyString2.toLowerCase())).split(";");
			for (int i = 1; i < line.length; i++) {
				String[] reg = bancoBinario.getData(Integer.parseInt(line[i]), structBanco).split(";");
				hashSet.add(reg);
			}
		}
		return new JSONArray(hashSet);
	}

	public void recompilar() throws IOException {
		// Files.delete(Paths.get("indice-invertido.txt"));
		// Files.delete(Paths.get("indice-hash.txt"));
		//
		// IndiceInvertido indice = new IndiceInvertido("banco.csv", 1,
		// ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
		// indice.toFile("indice-invertido.txt");
		TabelaHash indiceHash = new TabelaHash("indice-invertido.txt", 6373);
		indiceHash.criarArquivo("indice-hash.txt");

		bancoBinario.close();

		// Files.delete(Paths.get("banco.bin"));
		// Files.delete(Paths.get("indice-invertido.bin"));
		// Files.delete(Paths.get("indice-hash.bin"));

		bancoBinario = new RegistroBinario("banco.bin", structBanco, 60);
		indiceBinario = new RegistroBinario("indice-invertido.bin", null, 60);
		;
		hashBinario = new TabelaHashBinaria("indice-hash.bin", 6373);

		bancoBinario.fixedRegToBin("banco.csv", structBanco, ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
		indiceBinario.variableRegToBin("indice-invertido.txt", ";", 8400);

		RegistroBinario indiceHashBinario = new RegistroBinario("indice-hash.bin", structHash, 60);
		indiceHashBinario.fixedRegToBin("indice-hash.txt", structHash, ";");
		indiceHashBinario.close();
	}

	public void close() {
		bancoBinario.close();
		indiceBinario.close();
		hashBinario.close();
	}
}
