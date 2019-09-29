package listener;

import java.awt.Desktop;
import java.io.File;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import binario.RegistroBinario;
import indices.IndiceInvertido;
import indices.TabelaHash;
import indices.TabelaHashBinaria;

public class ListenerPort implements Container {
	
	FazAi fazAi = new FazAi();
	
	public void handle(Request request, Response response) {
		try {
			String path = request.getPath().getPath();
			System.out.println("Request: " + request.getQuery().toString());

			if (path.startsWith("/fazai")/* && "POST".equals(method)*/) {
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

		int porta = 1880;

		// Configura uma conexão soquete para o servidor HTTP.
		Container container = new ListenerPort();
		ContainerSocketProcessor servidor = new ContainerSocketProcessor(container);
		Connection conexao = new SocketConnection(servidor);
		SocketAddress endereco = new InetSocketAddress(porta);
		conexao.connect(endereco);

		//		Desktop.getDesktop().browse(new URI("https://pucweb-wesley-mouraria.azurewebsites.net/"));

		System.out.println("Interromper o servidor? (y/n)");

		Scanner ler = new Scanner(System.in);
		String a = "";
		while (!a.equals("y")) {
			a = ler.next();
			if (a.equals("n"))
				System.out.println("Ent�o n�o.");
			else if (!a.equals("y") && !a.equals("n"))
				System.out.println("Sem tempo irm�o.");
		}
		ler.close();
		conexao.close();
		servidor.stop();
		System.out.println("Servidor terminado.");
	}
}

class FazAi {

	RegistroBinario bancoBinario;
	RegistroBinario indiceBinario;
	TabelaHashBinaria hashBinario;

	private static String[] structBanco = {"int","String","String","String","String","String","float","float","float","float","float"};
	private static String[] structHash = {"String","int","int"};

	public FazAi() {
		bancoBinario = new RegistroBinario("banco.bin", structBanco, 60);
		indiceBinario = new RegistroBinario("indice-invertido.bin", null, 60);;
		hashBinario = new TabelaHashBinaria("indice-hash.bin", 6373);
	}

	public JSONArray fazONegocio(String keyString) {
		JSONArray json = new JSONArray();
		String[] line = indiceBinario.getData(
				hashBinario.getData(keyString)
					).split(";");
		for (int i = 1; i < line.length; i++) {
			String[] reg = bancoBinario.getData(Integer.parseInt(line[i]), structBanco).split(";");
			json.put(reg);
		}
		return json;
	}

	public void recompilar () {
		new File("indice-invertido.txt").delete();
		new File("indice-hash.txt").delete();

		IndiceInvertido indice = new IndiceInvertido("banco.csv", 1, ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
		indice.toFile("indice-invertido.txt");
		TabelaHash indiceHash = new TabelaHash("indice-invertido.txt" ,6373);
		indiceHash.criarArquivo("indice-hash.txt");

		new File("banco.bin").delete();
		new File("indice-invertido.bin").delete();
		new File("indice-hash.bin").delete();

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
