-- Dados de teste para validacao de filtros por dia, mes, categoria e tipo.
-- Pode executar varias vezes: os registros criados por este script sao removidos antes de reinserir.

INSERT IGNORE INTO categorias (nome) VALUES
('Salario'),
('Freelance'),
('Aluguel'),
('Mercado'),
('Transporte'),
('Contas'),
('Lazer'),
('Saude'),
('Educacao'),
('Investimentos');

DELETE FROM lancamentos WHERE comentario = 'seed-codex';

INSERT INTO lancamentos (descricao, valor, data, categoria, tipo, comentario) VALUES
('Salario Janeiro', 4200.00, '2026-01-05', 'Salario', 'RECEITA', 'seed-codex'),
('Aluguel Janeiro', 1450.00, '2026-01-06', 'Aluguel', 'DESPESA', 'seed-codex'),
('Mercado da semana', 286.75, '2026-01-12', 'Mercado', 'DESPESA', 'seed-codex'),
('Tesouro Direto', 500.00, '2026-01-20', 'Investimentos', 'INVESTIMENTO', 'seed-codex'),

('Salario Fevereiro', 4200.00, '2026-02-05', 'Salario', 'RECEITA', 'seed-codex'),
('Internet e telefone', 149.90, '2026-02-10', 'Contas', 'DESPESA', 'seed-codex'),
('Transporte mensal', 220.00, '2026-02-14', 'Transporte', 'DESPESA', 'seed-codex'),
('Cinema e jantar', 178.40, '2026-02-22', 'Lazer', 'DESPESA', 'seed-codex'),

('Salario Marco', 4200.00, '2026-03-05', 'Salario', 'RECEITA', 'seed-codex'),
('Projeto freelance', 950.00, '2026-03-11', 'Freelance', 'RECEITA', 'seed-codex'),
('Farmacia', 96.30, '2026-03-16', 'Saude', 'DESPESA', 'seed-codex'),
('CDB liquidez diaria', 700.00, '2026-03-28', 'Investimentos', 'INVESTIMENTO', 'seed-codex'),

('Salario Abril', 4200.00, '2026-04-05', 'Salario', 'RECEITA', 'seed-codex'),
('Supermercado compra grande', 512.18, '2026-04-08', 'Mercado', 'DESPESA', 'seed-codex'),
('Curso online', 189.90, '2026-04-18', 'Educacao', 'DESPESA', 'seed-codex'),
('Reserva de emergencia', 600.00, '2026-04-25', 'Investimentos', 'INVESTIMENTO', 'seed-codex'),

('Salario Maio', 4200.00, '2026-05-05', 'Salario', 'RECEITA', 'seed-codex'),
('Freelance landing page', 1300.00, '2026-05-09', 'Freelance', 'RECEITA', 'seed-codex'),
('Conta de luz', 238.67, '2026-05-13', 'Contas', 'DESPESA', 'seed-codex'),
('Restaurante familia', 264.80, '2026-05-24', 'Lazer', 'DESPESA', 'seed-codex'),

('Salario Junho', 4200.00, '2026-06-05', 'Salario', 'RECEITA', 'seed-codex'),
('Aluguel Junho', 1450.00, '2026-06-06', 'Aluguel', 'DESPESA', 'seed-codex'),
('Mercado meio do mes', 342.55, '2026-06-15', 'Mercado', 'DESPESA', 'seed-codex'),
('Aporte mensal', 800.00, '2026-06-26', 'Investimentos', 'INVESTIMENTO', 'seed-codex'),

('Bonus semestral', 1800.00, '2026-07-02', 'Salario', 'RECEITA', 'seed-codex'),
('Material faculdade', 320.00, '2026-07-12', 'Educacao', 'DESPESA', 'seed-codex'),
('Consulta medica', 250.00, '2026-08-03', 'Saude', 'DESPESA', 'seed-codex'),
('Fundo de investimento', 1000.00, '2026-08-19', 'Investimentos', 'INVESTIMENTO', 'seed-codex');