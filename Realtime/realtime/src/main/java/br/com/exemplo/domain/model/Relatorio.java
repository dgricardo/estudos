package br.com.exemplo.domain.model;


import br.com.exemplo.domain.enums.StatusRelatorio;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "relatorio")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Relatorio {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @EqualsAndHashCode.Include
    private Long id;
	
	@Column
	@Enumerated(EnumType.STRING)
	@NotNull
	private StatusRelatorio status;
}
