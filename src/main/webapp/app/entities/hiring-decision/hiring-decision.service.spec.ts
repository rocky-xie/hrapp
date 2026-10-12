import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { HiringDecision } from '@/shared/model/hiring-decision.model';

import HiringDecisionService from './hiring-decision.service';

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
  describe('HiringDecision Service', () => {
    let service: HiringDecisionService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new HiringDecisionService();
      currentDate = new Date();
      elemDefault = new HiringDecision(123, 'HIRED', 'AAAAAAA', 'AAAAAAA', currentDate, 'AAAAAAA', 0, false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { decidedAt: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
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

      it('should create a HiringDecision', async () => {
        const returnedFromService = { id: 123, decidedAt: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { decidedAt: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a HiringDecision', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a HiringDecision', async () => {
        const returnedFromService = {
          decision: 'BBBBBB',
          decisionSource: 'BBBBBB',
          decidedBy: 'BBBBBB',
          decidedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          reason: 'BBBBBB',
          correctionOfId: 1,
          active: true,
          ...elemDefault,
        };

        const expected = { decidedAt: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a HiringDecision', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a HiringDecision', async () => {
        const patchObject = { decision: 'BBBBBB', decidedBy: 'BBBBBB', reason: 'BBBBBB', correctionOfId: 1, ...new HiringDecision() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { decidedAt: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a HiringDecision', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of HiringDecision', async () => {
        const returnedFromService = {
          decision: 'BBBBBB',
          decisionSource: 'BBBBBB',
          decidedBy: 'BBBBBB',
          decidedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          reason: 'BBBBBB',
          correctionOfId: 1,
          active: true,
          ...elemDefault,
        };
        const expected = { decidedAt: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of HiringDecision', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a HiringDecision', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a HiringDecision', async () => {
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
