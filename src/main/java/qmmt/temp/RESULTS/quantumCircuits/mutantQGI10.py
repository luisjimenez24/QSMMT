# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI10 = QuantumCircuit(q0)

mutantQGI10.h(q0)
mutantQGI10.sx(q0)
mutantQGI10.z(q0)

