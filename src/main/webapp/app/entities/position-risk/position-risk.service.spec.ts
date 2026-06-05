import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { PositionRisk } from '@/shared/model/position-risk.model';

import PositionRiskService from './position-risk.service';

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
  describe('PositionRisk Service', () => {
    let service: PositionRiskService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PositionRiskService();
      currentDate = new Date();
      elemDefault = new PositionRisk(
        123,
        'SINGLE_POINT',
        'LOW',
        'AVAILABLE',
        'AVAILABLE',
        'HIGH',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        currentDate,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          identifiedDate: dayjs(currentDate).format(DATE_FORMAT),
          targetDate: dayjs(currentDate).format(DATE_FORMAT),
          closedDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
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

      it('should create a PositionRisk', async () => {
        const returnedFromService = {
          id: 123,
          identifiedDate: dayjs(currentDate).format(DATE_FORMAT),
          targetDate: dayjs(currentDate).format(DATE_FORMAT),
          closedDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { identifiedDate: currentDate, targetDate: currentDate, closedDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a PositionRisk', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a PositionRisk', async () => {
        const returnedFromService = {
          riskType: 'BBBBBB',
          riskLevel: 'BBBBBB',
          documentStatus: 'BBBBBB',
          backupStatus: 'BBBBBB',
          customerOrSystemDependency: 'BBBBBB',
          riskDescription: 'BBBBBB',
          improvementAction: 'BBBBBB',
          identifiedDate: dayjs(currentDate).format(DATE_FORMAT),
          targetDate: dayjs(currentDate).format(DATE_FORMAT),
          closedDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };

        const expected = { identifiedDate: currentDate, targetDate: currentDate, closedDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a PositionRisk', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a PositionRisk', async () => {
        const patchObject = {
          riskLevel: 'BBBBBB',
          documentStatus: 'BBBBBB',
          backupStatus: 'BBBBBB',
          customerOrSystemDependency: 'BBBBBB',
          improvementAction: 'BBBBBB',
          ...new PositionRisk(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { identifiedDate: currentDate, targetDate: currentDate, closedDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a PositionRisk', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of PositionRisk', async () => {
        const returnedFromService = {
          riskType: 'BBBBBB',
          riskLevel: 'BBBBBB',
          documentStatus: 'BBBBBB',
          backupStatus: 'BBBBBB',
          customerOrSystemDependency: 'BBBBBB',
          riskDescription: 'BBBBBB',
          improvementAction: 'BBBBBB',
          identifiedDate: dayjs(currentDate).format(DATE_FORMAT),
          targetDate: dayjs(currentDate).format(DATE_FORMAT),
          closedDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { identifiedDate: currentDate, targetDate: currentDate, closedDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of PositionRisk', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a PositionRisk', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a PositionRisk', async () => {
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
