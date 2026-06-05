import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { TrustObservation } from '@/shared/model/trust-observation.model';

import TrustObservationService from './trust-observation.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('TrustObservation Service', () => {
    let service: TrustObservationService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new TrustObservationService();
      currentDate = new Date();
      elemDefault = new TrustObservation(123, currentDate, 'S0_UNOBSERVED', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { observationDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a TrustObservation', async () => {
        const returnedFromService = { id: 123, observationDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { observationDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a TrustObservation', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a TrustObservation', async () => {
        const returnedFromService = {
          observationDate: dayjs(currentDate).format(DATE_FORMAT),
          trustStage: 'BBBBBB',
          observedBehavior: 'BBBBBB',
          positiveSignal: 'BBBBBB',
          riskSignal: 'BBBBBB',
          nextObservationPoint: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { observationDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a TrustObservation', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a TrustObservation', async () => {
        const patchObject = {
          observationDate: dayjs(currentDate).format(DATE_FORMAT),
          positiveSignal: 'BBBBBB',
          ...new TrustObservation(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { observationDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a TrustObservation', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of TrustObservation', async () => {
        const returnedFromService = {
          observationDate: dayjs(currentDate).format(DATE_FORMAT),
          trustStage: 'BBBBBB',
          observedBehavior: 'BBBBBB',
          positiveSignal: 'BBBBBB',
          riskSignal: 'BBBBBB',
          nextObservationPoint: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { observationDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of TrustObservation', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a TrustObservation', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a TrustObservation', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
