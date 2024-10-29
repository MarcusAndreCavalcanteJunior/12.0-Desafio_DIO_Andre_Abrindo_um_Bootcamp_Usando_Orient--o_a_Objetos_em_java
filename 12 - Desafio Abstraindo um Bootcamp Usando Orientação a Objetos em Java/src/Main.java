import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Bootcamp bootcamp = criarBootcamp();

        Dev andre = new Dev("André");
        Dev marcia = new Dev("Márcia");
        Dev rodrigo = new Dev("Rodrigo");

        andre.inscreverBootcamp(bootcamp);
        marcia.inscreverBootcamp(bootcamp);
        rodrigo.inscreverBootcamp(bootcamp);

        exibirProgresso(andre, 2);
        exibirProgresso(marcia, 2);
        exibirProgresso(rodrigo, 3);
    }

    private static Bootcamp criarBootcamp() {
        Bootcamp bootcamp = new Bootcamp("Bootcamp Java Developer", "Descrição Bootcamp Java Developer");
        bootcamp.adicionarConteudo(new Curso("Curso Java", "Descrição curso Java", 8));
        bootcamp.adicionarConteudo(new Curso("Curso JS", "Descrição curso JS", 4));
        bootcamp.adicionarConteudo(new Mentoria("Mentoria de Java", "Descrição mentoria Java", LocalDate.now()));

        System.out.printf("Bootcamp criado: %s - %s%n", bootcamp.getNome(), bootcamp.getDescricao());
        return bootcamp;
    }

    private static void exibirProgresso(Dev dev, int progresso) {
        for (int i = 0; i < progresso; i++)
            dev.progredir();
        System.out.printf("Conteúdos Concluídos %s: %s%n", dev.getNome(), dev.getConteudosConcluidos());
        System.out.printf("XP %s: %.2f%n", dev.getNome(), dev.calcularTotalXp());
    }
}

abstract class Conteudo {
    protected static final double XP_PADRAO = 10d;
    private final String titulo;
    private final String descricao;

    protected Conteudo(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public abstract double calcularXp();

    @Override
    public String toString() {
        return String.format("%s - %s", titulo, descricao);
    }
}

class Curso extends Conteudo {
    private final int cargaHoraria;

    public Curso(String titulo, String descricao, int cargaHoraria) {
        super(titulo, descricao);
        this.cargaHoraria = cargaHoraria;
    }

    @Override
    public double calcularXp() {
        return XP_PADRAO * cargaHoraria;
    }
}

class Mentoria extends Conteudo {
    private final LocalDate data;

    public Mentoria(String titulo, String descricao, LocalDate data) {
        super(titulo, descricao);
        this.data = data;
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public double calcularXp() {
        return XP_PADRAO + 20d;
    }

    @Override
    public String toString() {
        return super.toString() + " - Data: " + data;
    }
}

class Bootcamp {
    private final String nome;
    private final String descricao;
    private final Set<Conteudo> conteudos = new LinkedHashSet<>();

    public Bootcamp(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public void adicionarConteudo(Conteudo conteudo) {
        conteudos.add(conteudo);
    }

    public Set<Conteudo> getConteudos() {
        return conteudos;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}

class Dev {
    private final String nome;
    private final Set<Conteudo> conteudosInscritos = new LinkedHashSet<>();
    private final Set<Conteudo> conteudosConcluidos = new LinkedHashSet<>();

    public Dev(String nome) {
        this.nome = nome;
    }

    public void inscreverBootcamp(Bootcamp bootcamp) {
        conteudosInscritos.addAll(bootcamp.getConteudos());
    }

    public void progredir() {
        Optional<Conteudo> conteudo = conteudosInscritos.stream().findFirst();
        conteudo.ifPresent(c -> {
            conteudosConcluidos.add(c);
            conteudosInscritos.remove(c);
        });
    }

    public double calcularTotalXp() {
        return conteudosConcluidos.stream().mapToDouble(Conteudo::calcularXp).sum();
    }

    public String getNome() {
        return nome;
    }

    public Set<Conteudo> getConteudosConcluidos() {
        return conteudosConcluidos;
    }
}
