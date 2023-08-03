# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI1 = QuantumCircuit(q0)

mutantQGI1.h(q0)
mutantQGI1.t(q0)
mutantQGI1.z(q0)

