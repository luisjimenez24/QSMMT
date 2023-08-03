# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI11 = QuantumCircuit(q0)

mutantQGI11.h(q0)
mutantQGI11.z(q0)
mutantQGI11.t(q0)

